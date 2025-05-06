package kr.co.pei.pei_app.common.application.dto.surveyresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseCreateDTO {

    private String answerContent;
    private LocalDateTime submittedAt;

    // survey
    private Long surveyId;

    // ward
    private Long wardId;

    // depart
    private Long departId;

    // part info
    private String ageGroup;
    private String genderType;
}
