package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@Slf4j
@Tag(name = "AUTH_API", description = "클라이언트에서 인증/인가를 위해 서버 측에서 검증을 위한 API")
@RestController
@RequestMapping("/api/reissue")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "엑세스 토큰 재발급", description = "사용자의 엑세스 토큰이 만료 시, 리플래시 토큰을 검증 후 엑세스 토큰 재발급")
    @PostMapping
    public ResponseEntity<ApiResult<String>> reissueToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      log.info("토큰 검증 후 재발급 로직 실행");

      Cookie[] cookies = request.getCookies();

      String newAccessToken = authService.validRefreshFromCookieAndRedis(cookies);

      response.setHeader("Authorization",  "Bearer " + newAccessToken);

      return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success("토큰 재발급 성공"));
    }
}
