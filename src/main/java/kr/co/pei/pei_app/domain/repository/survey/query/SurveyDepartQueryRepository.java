package kr.co.pei.pei_app.domain.repository.survey.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SurveyDepartQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
}
