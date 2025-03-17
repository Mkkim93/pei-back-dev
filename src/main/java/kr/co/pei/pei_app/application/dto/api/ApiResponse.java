package kr.co.pei.pei_app.application.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@SuppressWarnings(value = "rawtypes")
@JsonInclude(JsonInclude.Include.NON_NULL) // 응답 시 null 필드 제외
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final String errorCode;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, null, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null, null);
    }

    public static <T> ApiResponse<T> error(int status, String errorCode, String message) {
        return new ApiResponse<>(status, errorCode, message, null);
    }

    public static <T> ApiResponse<T> error(int status, String errorCode, String message, T data) {
        return new ApiResponse<>(status, errorCode, message, data);
    }
}
