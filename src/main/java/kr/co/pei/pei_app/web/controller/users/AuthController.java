package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import kr.co.pei.pei_app.config.exception.ErrorResult;
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

    // TODO
    @Operation(summary = "엑세스 토큰 재발급", description = "사용자의 엑세스 토큰이 만료 시, 리플래시 토큰 검증 후 엑세스 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "쿠키/REDIS 리플래시 토큰 검증 성공 시 새로운 엑세스 토큰 발급",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "리플래시 토큰 검증 성공 후 엑세스 토큰 전송 예시",
                                    summary = "엑세스 토큰 헤더 전송 응답 예제",
                                    value = """
                                            {
                                                "status": 201,
                                                "message": "토큰 재발급 성공",
                                                "timestamp": "2025-03-26T17:54:37.862829",
                                                "data": true
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "리플래시 토큰 검증 실패 시 401 응답 클라이언트에서 로그인 컴포넌트로 유도",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "리플래시 토큰 검증 실패 예시",
                                    summary = "리플래시 토큰 검증 실패 응답 예제",
                                    value = """
                                            {
                                                "status": 401,
                                                "message": "인증이 만료 되었습니다.",
                                                "timeStamp": "2025-03-26T17:59:37.6658"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResult<Boolean>> reissueToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      log.info("토큰 검증 후 재발급 로직 실행");

      Cookie[] cookies = request.getCookies();

      String newAccessToken = authService.validRefreshFromCookieAndRedis(cookies);

      response.setHeader("Authorization",  "Bearer " + newAccessToken);

      return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(HttpStatus.CREATED.value(), "토큰 재발급 성공", true));
    }
}
