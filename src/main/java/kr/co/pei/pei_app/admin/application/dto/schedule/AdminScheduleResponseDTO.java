package kr.co.pei.pei_app.admin.application.dto.schedule;

import kr.co.pei.pei_app.domain.entity.schedule.Schedule;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminScheduleResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public AdminScheduleResponseDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
    }
}
