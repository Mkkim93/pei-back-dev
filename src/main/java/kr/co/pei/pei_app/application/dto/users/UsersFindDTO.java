package kr.co.pei.pei_app.application.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pei.pei_app.domain.entity.users.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 전체 사용자 목록 조회 DTO
 */
@Schema(description = "사용자 목록 조회 DTO")
@Data
@NoArgsConstructor
public class UsersFindDTO {

    private Long id;
    private String username;
    private String name;
    private String phone;
    private String mail;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    private String roleType;
    private String hospitalName;

    public UsersFindDTO(Long id, String username,
                        String name, String phone,
                        String mail, LocalDateTime createAt,
                        RoleType roleType, String hospitalName) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.createAt = createAt;
        this.roleType = roleType.getText();
        this.hospitalName = hospitalName;
    }
}
