package kr.co.pei.pei_app.admin.application.dto.surveys.response;

import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import lombok.Data;

import java.util.Map;

@Data
public class AdminSurveyResponseDTO {

    private Map<String, Object> answerContent;
    private Survey survey;
    private SurveyParticipant participant;
}
