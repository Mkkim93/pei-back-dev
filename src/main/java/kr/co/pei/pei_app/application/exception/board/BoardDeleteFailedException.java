package kr.co.pei.pei_app.application.exception.board;

public class BoardDeleteFailedException extends RuntimeException {

    public BoardDeleteFailedException(String message) {
        super(message);
    }

    public BoardDeleteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
