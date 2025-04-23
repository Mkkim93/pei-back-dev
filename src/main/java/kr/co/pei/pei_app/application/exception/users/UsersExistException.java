package kr.co.pei.pei_app.application.exception.users;

public class UsersExistException extends RuntimeException {
    public UsersExistException(String message) {
        super(message);
    }
}
