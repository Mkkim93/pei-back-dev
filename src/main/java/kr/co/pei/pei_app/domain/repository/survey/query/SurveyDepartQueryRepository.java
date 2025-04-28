package kr.co.pei.pei_app.domain.repository.survey.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.pei.pei_app.application.dto.survey.depart.FindDepartDTO;
import kr.co.pei.pei_app.domain.entity.survey.QSurveyDepart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyDepartQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
}
