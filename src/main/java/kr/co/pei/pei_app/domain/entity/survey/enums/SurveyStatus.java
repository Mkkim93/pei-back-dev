package kr.co.pei.pei_app.domain.entity.survey.enums;

public enum SurveyStatus {

    WAITING("대기"), CLOSED("완료"), ACTIVE("진행중");

    private String text;

    SurveyStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
