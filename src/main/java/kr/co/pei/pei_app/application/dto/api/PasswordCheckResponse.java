package kr.co.pei.pei_app.application.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "비밀번호 검증 및 등급 DTO")
public class PasswordCheckResponse {

    private String description;
    private boolean isStrongPassword;
    private int grade;
}
