package kr.co.pei.pei_app.domain.repository.surveyresponse;

import kr.co.pei.pei_app.domain.entity.surveyresponse.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseJpaRepository extends JpaRepository<SurveyResponse, Long> {
}
