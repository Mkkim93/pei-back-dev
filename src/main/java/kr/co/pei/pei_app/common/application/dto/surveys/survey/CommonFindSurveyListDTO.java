package kr.co.pei.pei_app.common.application.dto.surveys.survey;

import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommonFindSurveyListDTO {

    private Long id;
    private String title;
    private String surveyTypeName;
    private String category;
    private String description;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private String surveyStatus;

    public CommonFindSurveyListDTO(Long id, String title, String surveyTypeName,
                                   CategoryType category, String description, LocalDateTime openAt,
                                   LocalDateTime closeAt, SurveyStatus surveyStatus) {
        this.id = id;
        this.title = title;
        this.category = category.getText();
        this.description = description;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.surveyStatus = surveyStatus.getText();
        this.surveyTypeName = surveyTypeName;
    }
}
