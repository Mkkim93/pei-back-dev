package kr.co.pei.pei_app.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pei.pei_app.application.dto.users.LoginDTO;
import kr.co.pei.pei_app.application.service.redis.JwtRedisService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtRedisService redisService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final Long accessTokenExpired;
    private final Long refreshTokenExpired;
    private final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, JwtRedisService redisService,
                       Long accessTokenExpired, Long refreshTokenExpired, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.accessTokenExpired = accessTokenExpired;
        this.refreshTokenExpired = refreshTokenExpired;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("사용자 인증 로직 실행");
        try {

            LoginDTO loginDTO = objectMapper.readValue(
                    request.getInputStream(), LoginDTO.class);

            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password, null);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationException("사용자 인증에 실패하였습니다.") {};
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        log.info("인증 성공 토큰 발급 로직 실행");
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Long id = principal.getId();
        Long hospital = principal.getHospital();

        log.info("로그인 한사용자 PK: {}", id);

        String access = jwtUtil.createJwt("access", id, username, role, hospital, accessTokenExpired);

        String refresh = jwtUtil.createJwt("refresh", id, username, role, hospital, refreshTokenExpired);

        redisService.saveRefreshToken(username, refresh, refreshTokenExpired);

        response.setHeader("Authorization", "Bearer " + access);

        response.addCookie(createCookie("refresh", refresh));

        response.setStatus(HttpStatus.OK.value());
        sendSuccessResponse(response, "LOGIN_SUCCESS", "로그인에 성공 하였습니다.");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 10);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("인증 실패 로직 실행");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "LOGIN_FAILED", "아이디와 비밀번호를 확인해주세요.");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status);
        responseBody.put("message", message);
        responseBody.put("code",code);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(responseBody));
            writer.flush();
        }
    }

    /**
     * JSON 성공 응답을 반환하는 유틸리티 메서드
     */
    private void sendSuccessResponse(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpServletResponse.SC_OK);
        responseBody.put("message", message);
        responseBody.put("code", code);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(responseBody));
            writer.flush();
        }
    }
}
