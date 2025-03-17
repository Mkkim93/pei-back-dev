package kr.co.pei.pei_app.application.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordRequest {
    private String username;
    private String password;
}
