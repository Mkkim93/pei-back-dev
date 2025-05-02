package kr.co.pei.pei_app.admin.application.dto.surveys.survey;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminFindSurveyDTO {

    private Long id;
    private String title;
    private String category;

    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime openAt;

    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime closeAt;

    private boolean isVisible;

    private String surveyStatus;

    private String surveyTypeName;
    private String surveyDepartName;

    @QueryProjection
    public AdminFindSurveyDTO(Long id, String title, String category,
                              LocalDateTime createdAt, LocalDateTime openAt, LocalDateTime closeAt, boolean isVisible, String surveyStatus,
                              String surveyTypeName, String surveyDepartName) {
        this.id = id;
        this.title = title;
        this.category = CategoryType.valueOf(category).getText();
        this.createdAt = createdAt;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.isVisible = isVisible;
        this.surveyStatus = SurveyStatus.valueOf(surveyStatus).getText();
        this.surveyTypeName = surveyTypeName;
        this.surveyDepartName = surveyDepartName;
    }
}
