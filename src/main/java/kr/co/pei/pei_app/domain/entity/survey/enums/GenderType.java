package kr.co.pei.pei_app.domain.entity.survey.enums;

public enum GenderType {

    MALE("남성"), FEMALE("여성");

    private  String text;

    GenderType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
