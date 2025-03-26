package kr.co.pei.pei_app.application.service.auth;

import jakarta.servlet.http.Cookie;
import kr.co.pei.pei_app.application.dto.api.PasswordCheckResponse;
import kr.co.pei.pei_app.application.exception.redis.OtpStorageException;
import kr.co.pei.pei_app.application.service.redis.AuthRedisService;
import kr.co.pei.pei_app.application.service.redis.JwtRedisService;
import kr.co.pei.pei_app.application.service.sms.SmsService;
import kr.co.pei.pei_app.application.service.smtp.MailAuthService;
import kr.co.pei.pei_app.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final JwtRedisService jwtRedisService;
    private final AuthRedisService authRedisService;
    private final MailAuthService mailAuthService;
    private final SmsService smsService;
    private final PasswordService passwordService;
    private final Long accessExpired;

    public AuthService(JwtUtil jwtUtil, JwtRedisService jwtRedisService, AuthRedisService authRedisService,
                       MailAuthService mailAuthService, SmsService smsService, PasswordService passwordService,
                       @Value("${spring.jwt.access.expired}") Long accessExpired) {
        this.jwtUtil = jwtUtil;
        this.jwtRedisService = jwtRedisService;
        this.authRedisService = authRedisService;
        this.mailAuthService = mailAuthService;
        this.smsService = smsService;
        this.passwordService = passwordService;
        this.accessExpired = accessExpired;
    }

    public Long sendOTP(String phone) {

        String secretCode = generateAuthCode();

        try {
            Long expiration = authRedisService.setSmsCode(phone, secretCode);
            if (expiration == null || expiration <= 0) {
                throw new OtpStorageException("Redis OTP 저장 실패: expiration 유효하지 않음");
            }
            smsService.sendSms(phone, secretCode);
            return expiration;
        } catch (OtpStorageException e) {
            throw new OtpStorageException("Redis 연결 오류로 인증코드 저장 실패", e);
        }
    }

    public void sendPassword(String encodedPassword, String mail) {
        mailAuthService.sendPassword(encodedPassword, mail);
    }

    // TODO refresh 토큰으로 사용자 정보 조회로 변경
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

    private String getRefreshTokenFromRedis(String token) {
        return jwtRedisService.findRefreshByToken(token);
    }

    public String validRefreshFromCookieAndRedis(Cookie[] cookies) throws AuthenticationException {

        String refreshToken = null;

        if (cookies == null) {
            log.warn("쿠키 정보 없음");
            throw new AuthenticationException("인증이 만료 되었습니다.") {};
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        String validRedisByToken = getRefreshTokenFromRedis(refreshToken);

        if (!validRedisByToken.equals(refreshToken)) {
            log.warn("리플래시 토큰 교차 검증 실패");
            throw new AuthenticationException("인증이 만료 되었습니다.");
        }

        Boolean expired = jwtUtil.isExpired(refreshToken);

        if (expired) { // true : 토큰이 만료됨
            log.warn("리플래시 토큰 만료");
            throw new AuthenticationException("인증이 만료 되었습니다.");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        return jwtUtil.createJwt("access", username, role, accessExpired);
    }
}
