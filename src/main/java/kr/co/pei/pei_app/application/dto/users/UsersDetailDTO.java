package kr.co.pei.pei_app.application.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

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
