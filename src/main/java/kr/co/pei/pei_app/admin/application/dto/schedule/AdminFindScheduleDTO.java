package kr.co.pei.pei_app.admin.application.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
/**
 * 클라이언트와 협의 해야 하는 어노테이션
 * 단순히 null 필드를 감출 수 있지만,
 * 클라이언트에서 반환하는 응답 필드가 null 일 때 조건을 처리해서 ui 를 구현해야 할 수도 있음
 */
//@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public class AdminFindScheduleDTO {

    private Long id;
    private String title;
    private String description;

    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "YYYY-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    private String status;

    private String writer;

    public AdminFindScheduleDTO(Long id, String title, String description,
                                LocalDateTime startTime, LocalDateTime endTime, String status, String writer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.writer = writer;
    }
}
