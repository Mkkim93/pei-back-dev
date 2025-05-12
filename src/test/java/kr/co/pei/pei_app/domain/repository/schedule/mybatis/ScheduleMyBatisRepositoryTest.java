package kr.co.pei.pei_app.domain.repository.schedule.mybatis;

import kr.co.pei.pei_app.admin.application.dto.schedule.AdminCreateScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminFindScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminScheduleUpdateDTO;
import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ScheduleMyBatisRepositoryTest {

    @Autowired
    private ScheduleMyBatisRepository repository;

    @Test
    void save() {
        AdminCreateScheduleDTO dto = new AdminCreateScheduleDTO();
        dto.setTitle("일정 제목5");
        dto.setDescription("일정 내용5");
        dto.setStartTime(LocalDateTime.of(2025, 05, 9, 15, 30));
        dto.setEndTime(LocalDateTime.of(2025, 05, 20, 15, 30));
        dto.setStatus("진행중");
        dto.setUsersId(1L);
        repository.save(dto);
    }

    @Test
    void update() {
        AdminScheduleUpdateDTO updateDTO = new AdminScheduleUpdateDTO();
        updateDTO.setTitle("일정 제목6");
        updateDTO.setDescription("일정2 -> 6으로 변경");
        updateDTO.setId(2L);
        repository.update(updateDTO);
    }

    @Test
    void findById() {
        Long id = 2L;
        Optional<Schedule> result = repository.findById(id);
        Schedule schedule = result.get();
        System.out.println("schedule.getTitle() = " + schedule.getTitle());
    }

    @Test
    void findAll() {
        AdminFindScheduleDTO dto = new AdminFindScheduleDTO();
        List<AdminFindScheduleDTO> list = repository.findAll();
    }
}