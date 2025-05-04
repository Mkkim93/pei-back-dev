package kr.co.pei.pei_app.admin.application.dto.surveys.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminFindTypeRecentDTO {

    private Long id;
    private String name;
    private LocalDateTime updatedAt;
}
