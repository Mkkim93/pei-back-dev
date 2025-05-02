package kr.co.pei.pei_app.admin.application.exception.users;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
