package kr.co.pei.pei_app.application.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "API 공통 응답 처리 DTO (클라이언트의 잘못된 입력 값 error 처리)")
@Getter
@RequiredArgsConstructor
@SuppressWarnings(value = "rawtypes")
@JsonInclude(JsonInclude.Include.NON_NULL) // 응답 시 null 필드 제외
public class ApiResult<T> {

    private final int status;
    private final String message;
    private final String errorCode;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final T data;

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, null, data);
    }

    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(200, message, null, null);
    }

    public static <T> ApiResult<T> error(int status, String errorCode, String message) {
        return new ApiResult<>(status, errorCode, message, null);
    }

    public static <T> ApiResult<T> error(int status, String errorCode, String message, T data) {
        return new ApiResult<>(status, errorCode, message, data);
    }
}
