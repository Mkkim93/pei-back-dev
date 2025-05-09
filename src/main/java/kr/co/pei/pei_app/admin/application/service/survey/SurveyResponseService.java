package kr.co.pei.pei_app.admin.application.service.survey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pei.pei_app.admin.application.dto.surveys.response.AdminSurveyResponseDTO;
import kr.co.pei.pei_app.domain.entity.surveyresponse.SurveyResponse;
import kr.co.pei.pei_app.domain.repository.surveyresponse.SurveyResponseJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyResponseService {

    private final SurveyResponseJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;

    // TODO 먼지 까먹음..
    @Transactional(readOnly = true)
    public void saveSurveyResponse(AdminSurveyResponseDTO responseDTO) throws JsonProcessingException {

        try {

            String jsonContent = objectMapper.writeValueAsString(responseDTO.getAnswerContent());

            SurveyResponse entity = SurveyResponse.builder()
                    .answerContent(jsonContent)
                    .survey(responseDTO.getSurvey())
                    .surveyParticipant(responseDTO.getParticipant())
                    .build();
            jpaRepository.save(entity);

        } catch (Exception e) {
            log.info("json 파싱 오류 : {}", e.getMessage());
            throw new RuntimeException("설문 응답 저장 실패: ", e);
        }
    }
}
