package kr.co.pei.pei_app.domain.repository.survey.query;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.application.dto.surveys.survey.FindSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.QFindSurveyDTO;
import kr.co.pei.pei_app.domain.entity.survey.QSurvey;
import kr.co.pei.pei_app.domain.entity.survey.QSurveyDepart;
import kr.co.pei.pei_app.domain.entity.survey.QSurveyType;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.pei.pei_app.domain.entity.survey.QSurvey.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyDepart.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyType.*;

@Repository
@RequiredArgsConstructor
public class SurveyQueryRepository implements SurveyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    // 현재 로그인된 사용자의 소속 병원의 작성된 모든 설문 양식 조회
    @Override
    public Page<FindSurveyDTO> findMySurveyPage(Pageable pageable, Long hospitalId) {

        List<FindSurveyDTO> content = queryFactory.select(new QFindSurveyDTO(
                        survey.id,
                        survey.title,
                        survey.category.stringValue(),
                        survey.createdAt,
                        survey.openAt,
                        survey.closeAt,
                        survey.surveyStatus.stringValue(),
                        surveyType.name,
                        surveyDepart.name))
                .from(survey)
                .leftJoin(survey.surveyDepart, surveyDepart)
                .leftJoin(survey.surveyType, surveyType)
                .where(survey.hospital.id.eq(hospitalId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(survey.createdAt.asc())
                .fetch();

        JPAQuery<Survey> count = queryFactory
                .selectFrom(survey)
                .leftJoin(survey.surveyDepart, surveyDepart)
                .leftJoin(survey.surveyType, surveyType)
                .where(survey.hospital.id.eq(hospitalId));

        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchCount());
    }
}
