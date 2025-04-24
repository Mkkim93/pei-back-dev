package kr.co.pei.pei_app.config.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 비즈니스 로직 내부에서 발생한 '예외' 처리 객체
 * - GlobalControllerAdvice 에서 모든 '예외' 처리
 * 이제 안씀 ApiResult 로 통합
 */
@Schema(description = "비즈니스 로직 내부에서 발생한 예외 처리 (DB, SERVER 등) DTO")
@Data
public class ErrorResult {
    private int status;
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timeStamp;

    public ErrorResult(int status, Map<String, String> errors) {
        this.status = status;
        this.timeStamp = LocalDateTime.now();
        this.errors = errors;
    }

    public ErrorResult(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
}
