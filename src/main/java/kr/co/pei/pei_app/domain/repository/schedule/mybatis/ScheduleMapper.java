package kr.co.pei.pei_app.domain.repository.schedule.mybatis;

import kr.co.pei.pei_app.admin.application.dto.schedule.AdminCreateScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminFindScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminScheduleUpdateDTO;
import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper // 이 어노테이션을 적용해야 mapper 파일(.xml)에서 인식
public interface ScheduleMapper {

    // 일정 저장
    void save(AdminCreateScheduleDTO dto);

    // 일정 수정
    void update(@Param("update") AdminScheduleUpdateDTO dto);

    // 단일 일정 조회
    Optional<Schedule> findById(Long id);

    // 일정 전체 조회
    List<Schedule> findAll(AdminFindScheduleDTO dto);
}
