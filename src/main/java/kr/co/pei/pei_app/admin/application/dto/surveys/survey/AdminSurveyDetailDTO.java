package kr.co.pei.pei_app.admin.application.dto.surveys.survey;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType.valueOf;

/**
 * 설문 양식 상세 조회 + 수정 조회
 */
@Data
@NoArgsConstructor
public class AdminSurveyDetailDTO {

    private Long id;
    private String title;
    private String category;
    private Object content;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime openAt;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime closeAt;

    private Long surveyTypeId;
    private Long surveyDepartId;

    private String writer; // 설문 최초 작성자

    @QueryProjection
    public AdminSurveyDetailDTO(Long id, String title, String category, Object content, LocalDateTime createdAt,
                                LocalDateTime openAt, LocalDateTime closeAt, Long surveyTypeId, Long surveyDepartId, String writer) {
        this.id = id;
        this.title = title;
        this.category = valueOf(category).getText();
        this.content = content;
        this.createdAt = createdAt;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.surveyTypeId = surveyTypeId;
        this.surveyDepartId = surveyDepartId;
        this.writer = writer;
    }
}
