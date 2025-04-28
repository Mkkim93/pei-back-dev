package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyPartJpaRepository extends JpaRepository<SurveyParticipant, Long> {
}
