package kr.co.pei.pei_app.application.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 비밀번호 검증 DTO
 */
@Schema(description = "비밀번호 검증 DTO")
@Data
@AllArgsConstructor
public class PasswordRequest {
    private String username;
    private String password;
}
