package kr.co.pei.pei_app.domain.repository.survey.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SurveyResponseJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
}
