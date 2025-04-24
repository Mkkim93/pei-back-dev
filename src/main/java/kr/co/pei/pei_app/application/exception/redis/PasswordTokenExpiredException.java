package kr.co.pei.pei_app.application.exception.redis;

public class PasswordTokenExpiredException extends RuntimeException {
    public PasswordTokenExpiredException(String message) {
        super(message);
    }
}
