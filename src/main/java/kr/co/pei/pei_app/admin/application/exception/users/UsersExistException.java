package kr.co.pei.pei_app.admin.application.exception.users;

public class UsersExistException extends RuntimeException {
    public UsersExistException(String message) {
        super(message);
    }
}
