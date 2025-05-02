package kr.co.pei.pei_app.admin.application.exception.redis;

public class OtpStorageException extends RuntimeException {

    public OtpStorageException(String message) {
        super(message);
    }

    public OtpStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
