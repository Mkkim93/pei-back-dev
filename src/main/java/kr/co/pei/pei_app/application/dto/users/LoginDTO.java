package kr.co.pei.pei_app.application.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 로그인 DTO
 */
@Data
@NoArgsConstructor
public class LoginDTO {
    private String username;
    private String password;
}
