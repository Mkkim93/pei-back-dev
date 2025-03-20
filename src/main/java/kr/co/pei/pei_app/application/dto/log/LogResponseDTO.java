package kr.co.pei.pei_app.application.dto.log;

import com.fasterxml.jackson.annotation.JsonRawValue;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LogResponseDTO {

    private Long id;
    private String action;

    @JsonRawValue
    private String description;
    private LocalDateTime createdAt;
    private String users;

    public LogResponseDTO(Long id, String action,
                          String description, LocalDateTime createdAt, Users users) {
        this.id = id;
        this.action = action;
        this.description = description;
        this.createdAt = createdAt;
        this.users = users.getName();
    }
}
