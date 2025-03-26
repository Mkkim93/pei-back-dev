package kr.co.pei.pei_app.application.service.auth;

import kr.co.pei.pei_app.application.dto.api.PasswordCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordService {

    public PasswordCheckResponse calculateStrength(String password) {
        final int MIN_LENGTH = 8;
        final int STRONG_PASSWORD_THRESHOLD = 4;

        int strength = 0;
        boolean isStrongPassword;

        if (password.length() >= MIN_LENGTH) strength++;
        if (password.matches(".*\\d.*")) strength++; // 숫자 포함
        if (password.matches(".*[a-zA-Z].*")) strength++; // 영문 포함 (대/소문자 무관)
        if (password.matches(".*[!@#$%^&*()_+=].*")) strength++; // 특수문자 포함

        isStrongPassword = strength >= STRONG_PASSWORD_THRESHOLD;

        return new PasswordCheckResponse(getStrengthMessage(strength), isStrongPassword, strength);
    }

    public static String getStrengthMessage(int strength) {
        return switch (strength) {
            case 0, 1 -> "현재 비밀번호는 보안에 취약합니다. 최소 8자리 이상 숫자, 영문 특수문자를 조합하세요";
            case 2, 3 -> "현재 비밀번호는 보안에 취약합니다. 숫자, 영문, 특수문자를 조합하세요";
            case 4 -> "안전한 비밀번호 입니다.";
            default -> "사용 불가능한 비밀번호 입니다.";
        };
    }
}
