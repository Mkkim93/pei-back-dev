package kr.co.pei.pei_app.domain.repository.surveyresponse;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.QSurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.domain.entity.survey.QSurveyParticipant;
import kr.co.pei.pei_app.domain.entity.surveyresponse.QSurveyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kr.co.pei.pei_app.domain.entity.survey.QSurveyParticipant.*;

@Repository
@RequiredArgsConstructor
public class SurveyResponseQueryRepository implements SurveyResponseCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public SurveyResponseFindMetaDTO findMetaData(Long hospitalId) {

        return null;
    }
}
