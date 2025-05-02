package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminSurveyDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminFindSurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyRepositoryCustom {

    Page<AdminFindSurveyDTO> findMySurveyPage(Pageable pageable, Long hospitalId);

    AdminSurveyDetailDTO findSurveyDetail(Long id, Long hospitalId);
}
