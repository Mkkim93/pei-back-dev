package kr.co.pei.pei_app.domain.repository.survey.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminTypeStatusDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminTypeUsageDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.QAdminTypeStatusDTO;
import kr.co.pei.pei_app.domain.entity.hospital.QHospital;
import kr.co.pei.pei_app.domain.entity.survey.*;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyTypeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.dsl.Wildcard.count;
import static kr.co.pei.pei_app.domain.entity.hospital.QHospital.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurvey.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyDepart.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyType.surveyType;

@Repository
@RequiredArgsConstructor
public class SurveyTypeQueryRepository implements SurveyTypeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminTypeUsageDTO> findUsagePage(Pageable pageable, Long hospitalId, boolean isPublic) {
        QSurveyType st = surveyType;
        QSurvey s = survey;

        List<AdminTypeUsageDTO> content = queryFactory
                .select(Projections.constructor(
                        AdminTypeUsageDTO.class,
                        st.id,
                        st.name,
                        st.createdAt,
                        st.updatedAt,
                        st.deletedAt,
                        JPAExpressions.select(s.count())
                                .from(s)
                                .where(
                                        s.surveyType.id.eq(st.id))
                ))
                .from(st)
                .where(
                        isPublicEq(isPublic, hospitalId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<SurveyType> count = queryFactory
                .selectFrom(st)
                .where(isPublicEq(isPublic, hospitalId));

        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchCount());
    }

    public BooleanExpression isPublicEq(boolean isPublic, Long hospitalId) {
        return !isPublic ? surveyType.hospital.id.notIn(hospitalId)
                        .and(surveyType.isPublic.eq(true)) : surveyType.hospital.id.eq(hospitalId);
    }

    @Override
    public Page<AdminTypeStatusDTO> findStatusPage(Pageable pageable, Long hospitalId, boolean isPublic, String surveyStatus) {

        BooleanExpression condition = surveyType.isPublic.eq(isPublic);

        if (!isPublic) {
            condition = survey.hospital.id.eq(hospitalId);
        }

        List<AdminTypeStatusDTO> content;

        JPAQuery<AdminTypeStatusDTO> query = queryFactory
                .select(new QAdminTypeStatusDTO(
                        surveyType.id,
                        surveyType.name,
                        surveyType.description,
                        survey.title,
                        survey.category.stringValue(),
                        survey.surveyStatus.stringValue(),
                        survey.openAt,
                        survey.closeAt,
                        survey.createdAt,
                        survey.deletedAt, survey.updatedAt,
                        survey.isVisible, surveyDepart.name, hospital.name
                )).from(surveyType);

        if (!isPublic) {
            content = query
                    .join(survey).on(survey.surveyType.eq(surveyType))
                    .join(survey.surveyDepart, surveyDepart)
                    .join(survey.hospital, hospital)
                    .where(condition.and(survey.surveyStatus.eq(SurveyStatus.valueOf(surveyStatus))))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            content = query
                    .leftJoin(survey).on(survey.surveyType.eq(surveyType)
                            .and(survey.surveyStatus.eq(SurveyStatus.valueOf(surveyStatus))))
                    .leftJoin(survey.surveyDepart, surveyDepart)
                    .leftJoin(survey.hospital, hospital)
                    .where(surveyType.isPublic.isTrue())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        JPAQuery<Long> countQuery = queryFactory
                .select(surveyType.count())
                .from(surveyType);

            if (!isPublic) {
                countQuery
                        .join(survey).on(survey.surveyType.eq(surveyType))
                        .join(survey.surveyDepart, surveyDepart)
                        .join(survey.hospital, hospital)
                        .where(
                                survey.hospital.id.eq(hospitalId)
                                        .and(survey.surveyStatus.eq(SurveyStatus.valueOf(surveyStatus)))
                        );

        } else {
            countQuery.leftJoin(surveyType).on(survey.surveyType.eq(surveyType)
                            .and(survey.surveyStatus.eq(SurveyStatus.valueOf(surveyStatus))))
                    .where(surveyType.isPublic.isTrue());
        }

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
