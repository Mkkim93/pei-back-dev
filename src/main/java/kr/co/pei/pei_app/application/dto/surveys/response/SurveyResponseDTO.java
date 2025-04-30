package kr.co.pei.pei_app.application.dto.surveys.response;

import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import lombok.Data;

import java.util.Map;

@Data
public class SurveyResponseDTO {

    private Map<String, Object> answerContent;
    private Survey survey;
    private SurveyParticipant participant;
}
