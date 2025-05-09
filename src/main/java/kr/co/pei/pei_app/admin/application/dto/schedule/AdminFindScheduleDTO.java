package kr.co.pei.pei_app.admin.application.dto.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminFindScheduleDTO {

    private Long id;
    private String title;
    private String description;

}
