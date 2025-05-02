package kr.co.pei.pei_app.admin.application.exception.board;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
