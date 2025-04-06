package kr.co.pei.pei_app.domain.repository.schedule;

import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
