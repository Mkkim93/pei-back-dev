package kr.co.pei.pei_app.application.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordCheckResponse {

    private String description;
    private boolean isStrongPassword;
    private int grade;
}
