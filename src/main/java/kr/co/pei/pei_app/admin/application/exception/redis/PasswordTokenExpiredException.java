package kr.co.pei.pei_app.admin.application.exception.redis;

public class PasswordTokenExpiredException extends RuntimeException {
    public PasswordTokenExpiredException(String message) {
        super(message);
    }
}
