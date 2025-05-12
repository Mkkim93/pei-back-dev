package kr.co.pei.pei_app.admin.application.dto.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminCreateScheduleDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    // Users 엔티티가 들어올 수 없음
    private Long usersId;
    private Long hospitalId;
}
