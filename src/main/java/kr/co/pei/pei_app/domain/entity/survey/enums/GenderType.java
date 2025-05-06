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

    public static GenderType fromText(String text) {
        for (GenderType gender : GenderType.values()) {
            if (gender.getText().equals(text)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown age group: " + text);
    }
}
