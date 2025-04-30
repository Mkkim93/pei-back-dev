package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.application.dto.surveys.survey.FindSurveyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyRepositoryCustom {

    Page<FindSurveyDTO> findMySurveyPage(Pageable pageable, Long hospitalId);
}
