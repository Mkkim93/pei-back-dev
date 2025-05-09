package kr.co.pei.pei_app.admin.application.dto.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminScheduleUpdateDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime updatedAt;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isDeleted;
}
