package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonFindSurveyListDTO;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class SurveyJpaRepositoryTest {

    @Autowired
    private SurveyJpaRepository jpaRepository;

    @Test
    @DisplayName("리스트 조회")
    void findAllActiveSurveyPage() {
        Long hospitalId = 26L;
        PageRequest page = PageRequest.of(0, 10);
        SurveyStatus status = SurveyStatus.ACTIVE;
        Page<CommonFindSurveyListDTO> result = jpaRepository.findAllByActiveSurveyPage(page, status, hospitalId);

        System.out.println("진행중인 설문 양식 목록: " + result.getContent());
    }
}