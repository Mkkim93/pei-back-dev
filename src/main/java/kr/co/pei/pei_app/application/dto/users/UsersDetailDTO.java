package kr.co.pei.pei_app.application.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 내 정보 조회 DTO
 */
@Schema(description = "내 정보 조회를 위한 DTO")
@Data
@NoArgsConstructor
public class UsersDetailDTO {

    private String username;
    private String name;
    private String phone;
    private String mail;

    public UsersDetailDTO(String username, String name, String phone, String mail) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
    }
}
