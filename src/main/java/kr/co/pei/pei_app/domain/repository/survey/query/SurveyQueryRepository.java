package kr.co.pei.pei_app.domain.repository.survey.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminSurveyDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminFindSurveyDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.QAdminSurveyDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.QAdminFindSurveyDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.survey.CommonDetailSurveyDTO;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static kr.co.pei.pei_app.domain.entity.survey.QSurvey.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyDepart.*;
import static kr.co.pei.pei_app.domain.entity.survey.QSurveyType.*;
import static kr.co.pei.pei_app.domain.entity.users.QUsers.*;

@Repository
@RequiredArgsConstructor
public class SurveyQueryRepository implements SurveyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private final ObjectMapper mapper;

    // 현재 로그인된 사용자의 소속 병원의 작성된 모든 설문 양식 조회
    @Override
    public Page<AdminFindSurveyDTO> findMySurveyPage(Pageable pageable, Long hospitalId) {

        List<AdminFindSurveyDTO> content = queryFactory.select(new QAdminFindSurveyDTO(
                        survey.id,
                        survey.title,
                        survey.category.stringValue(),
                        survey.createdAt,
                        survey.openAt,
                        survey.closeAt,
                        survey.isVisible,
                        survey.surveyStatus.stringValue(),
                        surveyType.name,
                        surveyDepart.name))
                .from(survey)
                .leftJoin(survey.surveyDepart, surveyDepart)
                .leftJoin(survey.surveyType, surveyType)
                .where(survey.hospital.id.eq(hospitalId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(survey.createdAt.desc())
                .fetch();

        JPAQuery<Survey> count = queryFactory
                .selectFrom(survey)
                .leftJoin(survey.surveyDepart, surveyDepart)
                .leftJoin(survey.surveyType, surveyType)
                .where(survey.hospital.id.eq(hospitalId));

        return PageableExecutionUtils.getPage(content, pageable, () -> count.fetchCount());
    }

    @Override
    public AdminSurveyDetailDTO findSurveyDetail(Long id, Long hospitalId) {

        AdminSurveyDetailDTO dto = queryFactory.select(new QAdminSurveyDetailDTO(
                        survey.id,
                        survey.title,
                        survey.category.stringValue(),
                        survey.content,
                        survey.createdAt,
                        survey.openAt,
                        survey.closeAt,
                        survey.surveyDepart.id,
                        survey.surveyType.id,
                        users.name))
                .from(survey)
                .leftJoin(survey.users, users)
                .where(
                        survey.id.eq(id)
                                .and(survey.hospital.id.eq(hospitalId))
                ).fetchOne();

        if (dto != null && dto.getContent() instanceof String str) {
            try {
                Map<String, Object> json = mapper.readValue(str, new TypeReference<>() {});
                dto.setContent(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("설문 content JSON 파싱 실패", e);
            }
        }
        return dto;
    }

    @Override
    public CommonDetailSurveyDTO commonFindSurveyDetail(Long id) {


        return null;
    }
}
