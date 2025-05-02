package kr.co.pei.pei_app.admin.application.dto.log;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "활동 로그 응답 DTO")
@Data
@NoArgsConstructor
public class AdminLogResponseDTO {

    private Long id;
    private String action;

    @JsonRawValue
    private String description;
    private LocalDateTime createdAt;
    private String users;

    public AdminLogResponseDTO(Long id, String action,
                               String description, LocalDateTime createdAt, String users) {
        this.id = id;
        this.action = action;
        this.description = description;
        this.createdAt = createdAt;
        this.users = users;
    }
}
