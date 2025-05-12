package kr.co.pei.pei_app.domain.repository.schedule.mybatis;

import kr.co.pei.pei_app.admin.application.dto.schedule.AdminCreateScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminFindScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminScheduleUpdateDTO;
import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleMyBatisRepository implements ScheduleMapper {

    private final ScheduleMapper scheduleMapper;

    @Override
    public void save(AdminCreateScheduleDTO dto) {
        scheduleMapper.save(dto);
    }

    @Override
    public int update(AdminScheduleUpdateDTO dto) {
        return scheduleMapper.update(dto);
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleMapper.findById(id);
    }

    @Override
    public List<AdminFindScheduleDTO> findAll() {
        return scheduleMapper.findAll();
    }

    @Override
    public int deleteById(Long id) {
        return scheduleMapper.deleteById(id);
    }
}
