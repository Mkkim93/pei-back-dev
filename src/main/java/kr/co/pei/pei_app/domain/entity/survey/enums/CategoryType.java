package kr.co.pei.pei_app.domain.entity.survey.enums;

public enum CategoryType {
   OUTPATIENT("외래"), HOSPITALIZATION("입원");

   private final String text;

    CategoryType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
