package kr.co.pei.pei_app.domain.repository.surveyresponse;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.admin.application.dto.surveys.response.SurveyResponseDetailDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.QSurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.domain.entity.hospital.QHospital;
import kr.co.pei.pei_app.domain.entity.hospital.QWard;
import kr.co.pei.pei_app.domain.entity.survey.QSurvey;
import kr.co.pei.pei_app.domain.entity.survey.QSurveyParticipant;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import kr.co.pei.pei_app.domain.entity.surveyresponse.QSurveyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.pei.pei_app.domain.entity.hospital.QHospital.*;
import static kr.co.pei.pei_app.domain.entity.hospital.QWard.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurvey.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyParticipant.*;
import static kr.co.pei.pei_app.domain.entity.surveyresponse.QSurveyResponse.*;

@Repository
@RequiredArgsConstructor
public class SurveyResponseQueryRepository implements SurveyResponseCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public SurveyResponseFindMetaDTO findMetaData(Long hospitalId) {

        return null;
    }

    @Override
    public List<SurveyResponseDetailDTO> findDetailData(Long surveyId, Long hospitalId, SurveyStatus surveyStatus) {

        List<SurveyResponseDetailDTO> list = queryFactory.select(
                Projections.constructor(SurveyResponseDetailDTO.class,
                        surveyResponse.id,
                        survey.title,
                        survey.category.stringValue(),
//                        survey.content,
                        surveyResponse.answerContent,
                        survey.id,
                        hospital.id,
                        surveyParticipant.id,
                        ward.id
                )).from(surveyResponse)
                .leftJoin(surveyResponse.survey, survey)
                .leftJoin(surveyResponse.surveyParticipant, surveyParticipant)
                .leftJoin(surveyResponse.ward, ward)
                .leftJoin(survey.hospital, hospital)
                .where(
                        survey.id.eq(surveyId)
                                .and(survey.hospital.id.eq(hospitalId))
                                .and(survey.surveyStatus.eq(surveyStatus)))
                .groupBy(surveyResponse.id, survey.content).fetch();

        return list;
    }
}
