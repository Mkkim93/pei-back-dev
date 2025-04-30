package kr.co.pei.pei_app.application.exception.hospital;

public class HospitalExistException extends RuntimeException {
    public HospitalExistException(String message) {
        super(message);
    }
}
