package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.domain.entity.surveyres.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseJpaRepository extends JpaRepository<SurveyResponse, Long> {
}
