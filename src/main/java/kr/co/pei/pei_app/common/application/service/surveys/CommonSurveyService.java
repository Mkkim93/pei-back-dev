package kr.co.pei.pei_app.common.application.service.surveys;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonDetailSurveyDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonFindSurveyListDTO;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyJpaRepository;
import kr.co.pei.pei_app.domain.repository.survey.query.SurveyQueryRepository;
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
    private final SurveyQueryRepository queryRepository;

    // 현재 진행 중인 설문 조회
    public Page<CommonFindSurveyListDTO> findAllActiveSurveyPage(Pageable pageable, Long hospitalId, String statue) {
        return jpaRepository.findAllByActiveSurveyPage(pageable, SurveyStatus.valueOf(statue), hospitalId);
    }

    // 작성할 설문 상세 조회
    public CommonDetailSurveyDTO findDetailSurvey(Long hospitalId, Long surveyId) {
     return queryRepository.commonFindSurveyDetail(hospitalId, surveyId);
    }

    public Survey findBySurveyId(Long surveyId) {
        return jpaRepository.findById(surveyId).orElseThrow(() -> new EntityNotFoundException("해당 설문 아이디가 존재하지 않습니다."));
    }
}
