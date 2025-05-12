package kr.co.pei.pei_app.admin.application.service.schedule;

import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import kr.co.pei.pei_app.domain.repository.schedule.mybatis.ScheduleMyBatisRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleService service;

    @Autowired
    private ScheduleMyBatisRepository repository;

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    @DisplayName("일정 삭제 테스트")
    void delete() {

        // given
        Long id = 2L;

        // when
        service.delete(id);

        // then
        Schedule deletedCheckById = repository.findById(id).get();
        assertThat(deletedCheckById.isDeleted()).isTrue();
    }
}