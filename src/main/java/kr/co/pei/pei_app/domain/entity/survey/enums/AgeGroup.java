package kr.co.pei.pei_app.domain.entity.survey.enums;

public enum AgeGroup {

    AGE_10S("10대"),
    AGE_20S("20대"),
    AGE_30S("30대"),
    AGE_40S("40대"),
    AGE_50S("50대"),
    AGE_60S("60대"),
    AGE_70S("70대"),
    AGE_80S("80대"),
    AGE_90S("90대");

    private String text;

    AgeGroup(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
