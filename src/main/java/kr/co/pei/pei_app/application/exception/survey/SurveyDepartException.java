package kr.co.pei.pei_app.application.exception.survey;

public class SurveyDepartException extends RuntimeException {
    public SurveyDepartException(String message) {
        super(message);
    }

    public SurveyDepartException(String message, Throwable cause) {
        super(message, cause);
    }
}
