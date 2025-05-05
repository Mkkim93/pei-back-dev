package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminSurveyDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminFindSurveyDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonDetailSurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyRepositoryCustom {

    Page<AdminFindSurveyDTO> findMySurveyPage(Pageable pageable, Long hospitalId);

    AdminSurveyDetailDTO findSurveyDetail(Long id, Long hospitalId);

    // 사용자 전용 작성 설문 조회
    CommonDetailSurveyDTO commonFindSurveyDetail(Long id);

}
