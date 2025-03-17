package kr.co.pei.pei_app.config.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timeStamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
}
