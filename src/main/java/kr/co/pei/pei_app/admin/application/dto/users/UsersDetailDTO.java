package kr.co.pei.pei_app.admin.application.dto.users;

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
    private String roleType;
    private String description;
    private String userImg;
    private String hospitalName;

    public UsersDetailDTO(String username, String name, String phone, String mail, String roleType,
                          String description, String userImg, String hospitalName) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.roleType = roleType;
        this.description = description;
        this.userImg = userImg;
        this.hospitalName = hospitalName;
    }
}
