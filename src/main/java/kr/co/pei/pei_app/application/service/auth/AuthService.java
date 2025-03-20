package kr.co.pei.pei_app.application.service.auth;

import kr.co.pei.pei_app.application.dto.api.PasswordCheckResponse;
import kr.co.pei.pei_app.application.service.redis.AuthRedisService;
import kr.co.pei.pei_app.application.service.redis.JwtRedisService;
import kr.co.pei.pei_app.application.service.sms.SmsService;
import kr.co.pei.pei_app.application.service.smtp.MailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtRedisService jwtRedisService;
    private final AuthRedisService authRedisService;
    private final MailAuthService mailAuthService;
    private final SmsService smsService;
    private final PasswordService passwordService;

    public Long sendOTP(String phone) {

        String secretCode = generateAuthCode();
        Long expiration = authRedisService.setSmsCode(phone, secretCode);

        if (expiration > 0) {
            smsService.sendSms(phone, secretCode);
        } else {
            throw new IllegalStateException("OTP 저장에 실패하였습니다.");
        }
        return expiration;
    }

    public void sendPassword(String encodedPassword, String mail) {
        mailAuthService.sendPassword(encodedPassword, mail);
    }

    // refresh 토큰으로 사용자 정보 조회
    public String findUsernameByToken(String username) {
        return jwtRedisService.findUsernameByToken(username);
    }

    public Map<String, Object> getSecretPhoneCode(String phone) {
        return authRedisService.getSecretPhoneCode(phone);
    }

    public static String generateAuthCode() {
        SecureRandom randomCode = new SecureRandom();
        int code = 10000 + randomCode.nextInt(900000);
        return String.valueOf(code);
    }

    public void getUsernameByCode(String phone, String code) {
        authRedisService.getUsernameByCode(phone, code);
    }

    public PasswordCheckResponse checkPassword(String password) {
        return passwordService.calculateStrength(password);
    }
}
