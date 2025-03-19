package kr.co.pei.pei_app.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pei.pei_app.application.service.redis.JwtRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final JwtRedisService redisService;
    private final ObjectMapper objectMapper;

    public CustomLogoutFilter(JwtUtil jwtUtil, JwtRedisService redisService, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request,
                          ServletResponse response,
                          FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = httpRequest.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, "REFRESH_TOKEN_NULL","Refresh 토큰이 없습니다.");
            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, "REFRESH_TOKEN_EXPIRED","Refresh 토큰이 만료 되었습니다.");
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, "REFRESH_TOKEN_FAILED","잘못된 Refresh 토큰 입니다.");
            return;
        }

        String username = jwtUtil.getUsername(refresh);
        Boolean isExist = redisService.getRefreshTokenTTL(username);
        if (!isExist) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_BAD_REQUEST, "REFRESH_TOKEN_NULL","해당 사용자의 Refresh 토큰이 존재하지 않습니다.");
            return;
        }

        Boolean deletedByToken = redisService.deleteRefreshToken(username);
        if (!deletedByToken) {
            log.warn("로그아웃 경고: 사용자 {}의 Refresh Token 삭제 실패", username);
        }

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpResponse.addCookie(cookie);

        sendSuccessResponse(httpResponse, "LOGOUT_SUCCESS", "로그아웃이 완료 되었습니다.");
    }

    /**
     * JSON 에러 응답을 반환하는 유틸리티 메서드
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", response.getStatus());
        responseBody.put("message", message);
        responseBody.put("code", code);

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
        responseBody.put("status", response.getStatus());
        responseBody.put("message", message);
        responseBody.put("code", code);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(responseBody));
            writer.flush();
        }
    }
}
