package kr.co.pei.pei_app.common.application.service.surveys;

import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonDetailSurveyDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonFindSurveyListDTO;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonSurveyService {

    private final SurveyJpaRepository jpaRepository;

    // 현재 진행 중인 설문 조회
    public Page<CommonFindSurveyListDTO> findAllActiveSurveyPage(Pageable pageable, Long hospitalId, String statue) {
        return jpaRepository.findAllByActiveSurveyPage(pageable, SurveyStatus.valueOf(statue), hospitalId);
    }

    // 작성할 설문 상세 조회
    public CommonDetailSurveyDTO findDetailSurvey(Long surveyId) {

        return null;
    }
}
