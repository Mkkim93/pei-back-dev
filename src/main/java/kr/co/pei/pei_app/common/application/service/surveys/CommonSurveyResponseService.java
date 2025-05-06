package kr.co.pei.pei_app.common.application.service.surveys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseCreateDTO;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import kr.co.pei.pei_app.domain.entity.surveyresponse.SurveyResponse;
import kr.co.pei.pei_app.domain.repository.surveyresponse.SurveyResponseJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommonSurveyResponseService {

    private final SurveyResponseJpaRepository jpaRepository;

    public void saveResponse(SurveyResponseCreateDTO dto, Survey survey, Ward ward, SurveyParticipant participant) {

            SurveyResponse entity = SurveyResponse.builder()
                    .answerContent(dto.getAnswerContent())
                    .submittedAt(dto.getSubmittedAt())
                    .survey(survey)
                    .surveyParticipant(participant)
                    .ward(ward)
                    .build();

            jpaRepository.save(entity);
    }
}
