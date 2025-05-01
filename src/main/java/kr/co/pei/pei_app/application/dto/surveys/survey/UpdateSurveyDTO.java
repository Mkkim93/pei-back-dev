package kr.co.pei.pei_app.application.dto.surveys.survey;

import kr.co.pei.pei_app.domain.entity.survey.Survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

// 양식 업데이트 DTO 객체
@Data
@NoArgsConstructor
public class UpdateSurveyDTO {

    private Long id;
    private String category;
    private String title;
    private Map<String, Object> content;
    private LocalDateTime updatedAt;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private Long surveyDepartId;
    private Long surveyTypeId;
}
