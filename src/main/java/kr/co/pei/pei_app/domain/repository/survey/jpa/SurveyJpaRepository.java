package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonFindSurveyListDTO;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyJpaRepository extends JpaRepository<Survey, Long> {

    @Query("""
    select new kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonFindSurveyListDTO(
        s.id, s.title, st.name, s.category, st.description, s.openAt, s.closeAt, s.surveyStatus
    )
    from Survey s
    join s.surveyType st
    where s.surveyStatus = :status and s.hospital.id = :hospitalId
    """)
    Page<CommonFindSurveyListDTO> findAllByActiveSurveyPage(Pageable pageable, @Param("status")SurveyStatus status, @Param("hospitalId") Long hospitalId);
}
