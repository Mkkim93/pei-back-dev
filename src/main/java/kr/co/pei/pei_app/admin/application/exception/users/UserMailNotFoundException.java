package kr.co.pei.pei_app.admin.application.exception.users;

public class UserMailNotFoundException extends RuntimeException{
    public UserMailNotFoundException(String message) {
        super(message);
    }
}
