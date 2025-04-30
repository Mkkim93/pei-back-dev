package kr.co.pei.pei_app.application.dto.surveys.survey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class CreateSurveyDTO {

    private String title;
    private String category;
    private String content;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private Long surveyTypeId;
    private Long hospitalId;
    private Long surveyDepartId;
    private Long usersId; // 양식을 작성한 user
}
