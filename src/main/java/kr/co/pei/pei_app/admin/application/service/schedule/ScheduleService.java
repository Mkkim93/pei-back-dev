package kr.co.pei.pei_app.admin.application.service.schedule;

import kr.co.pei.pei_app.admin.application.dto.schedule.AdminCreateScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminFindScheduleDTO;
import kr.co.pei.pei_app.admin.application.dto.schedule.AdminScheduleUpdateDTO;
import kr.co.pei.pei_app.admin.application.exception.schedule.ScheduleUpdateException;
import kr.co.pei.pei_app.admin.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import kr.co.pei.pei_app.domain.repository.schedule.mybatis.ScheduleMyBatisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final UsersContextService contextService;
    private final ScheduleMyBatisRepository repository;

    public List<AdminFindScheduleDTO> findAll() {
        return repository.findAll();
    }

    public void save(AdminCreateScheduleDTO dto) {
        Long userId = contextService.getCurrentUser().getId();
        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        dto.setUsersId(userId);
        dto.setHospitalId(hospitalId);
        log.info("스케줄 등록 사용자 usersId : {}", userId);
        repository.save(dto);
    }

    public void delete(Long id) {
        int deleted = repository.deleteById(id);
        if (deleted < 0) {
            log.warn("일정 삭제 실패: {}", id + "에 실패하였습니다.");
            throw new ScheduleUpdateException("일정 삭제에 실패 하였습니다.");
        }
    }

    public void update(AdminScheduleUpdateDTO dto) {
        int updated = repository.update(dto);
        if (updated < 0) {
            log.warn("일정 수정 실패: {}", dto.getId() + "의 수정이 실패하였습니다.");
            throw new ScheduleUpdateException("일정 수정이 실패 하였습니다.");
        }
    }
}
