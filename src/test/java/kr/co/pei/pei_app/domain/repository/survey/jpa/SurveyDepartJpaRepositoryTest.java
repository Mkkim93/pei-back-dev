package kr.co.pei.pei_app.domain.repository.survey.jpa;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class SurveyDepartJpaRepositoryTest {

    @Autowired
    private SurveyDepartJpaRepository jpaRepository;

    @Test
    @DisplayName("진료과 수정 (등록 -> 미등록)")
    void modifyDepart() {
        List<Long> ids = new ArrayList<>();

        ids.add(22L);
        ids.add(23L);

        int deleted = jpaRepository.deleteDepartIds(ids);
        System.out.println(deleted);

    }
}