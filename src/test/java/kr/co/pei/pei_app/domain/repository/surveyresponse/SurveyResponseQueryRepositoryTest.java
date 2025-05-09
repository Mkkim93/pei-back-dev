package kr.co.pei.pei_app.domain.repository.surveyresponse;

import kr.co.pei.pei_app.admin.application.dto.surveys.response.SurveyResponseDetailDTO;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
class SurveyResponseQueryRepositoryTest {

    @Autowired
    private SurveyResponseQueryRepository queryRepository;

    @Test
    void findMetaData() {
    }

    @Test
    void findDetailData() {

        // given
        Long surveyId = 1L;
        Long hospitalId = 26L;
        SurveyStatus status = SurveyStatus.ACTIVE;

        // when
        List<SurveyResponseDetailDTO> result = queryRepository.findDetailData(surveyId, hospitalId, status);

        // then
        for (SurveyResponseDetailDTO dto : result) {
            System.out.println(dto.toString());
        }
    }
}