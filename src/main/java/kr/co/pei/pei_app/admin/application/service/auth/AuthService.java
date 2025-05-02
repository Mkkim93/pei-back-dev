package kr.co.pei.pei_app.admin.application.service.auth;

import jakarta.servlet.http.Cookie;
import kr.co.pei.pei_app.admin.application.exception.redis.OtpStorageException;
import kr.co.pei.pei_app.admin.application.service.redis.AuthRedisService;
import kr.co.pei.pei_app.admin.application.service.redis.JwtRedisService;
import kr.co.pei.pei_app.admin.application.service.sms.SmsService;
import kr.co.pei.pei_app.admin.application.service.smtp.MailAuthService;
import kr.co.pei.pei_app.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final JwtRedisService jwtRedisService;
    private final AuthRedisService authRedisService;
    private final MailAuthService mailAuthService;
    private final SmsService smsService;
    private final Long accessExpired;

    public AuthService(JwtUtil jwtUtil, JwtRedisService jwtRedisService, AuthRedisService authRedisService,
                       MailAuthService mailAuthService, SmsService smsService,
                       @Value("${spring.jwt.access.expired}") Long accessExpired) {
        this.jwtUtil = jwtUtil;
        this.jwtRedisService = jwtRedisService;
        this.authRedisService = authRedisService;
        this.mailAuthService = mailAuthService;
        this.smsService = smsService;
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

    public void sendPassword(Map<String, Object> savedMap) {
        mailAuthService.sendPassword(savedMap);
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

    private Map<String, Object> getRefreshTokenFromRedis(String token) {
        return jwtRedisService.existByToken(token);
    }

    public Map<String, Object> validRefreshFromCookieAndRedis(Cookie[] cookies)
            throws AuthenticationException {

        String refreshToken = null;

        if (cookies == null) {
            log.warn("쿠키 정보 없음");
            // 여기서는 쿠키 만료 시 인증 만료만 띄워야됨 레디스 토큰 삭제할 방법 없음 (헤더 사용자 정보 없음)
            throw new AuthenticationException("인증이 만료 되었습니다.") {};
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        Map<String, Object> validRedisByToken = getRefreshTokenFromRedis(refreshToken);

        String existToken = Optional.ofNullable(validRedisByToken.get("existToken"))
                .map(Object::toString)
                .orElse("");

        if (!existToken.equals(refreshToken) || existToken.isEmpty()) {
            log.warn("리플래시 토큰 교차 검증 실패 또는 레디스 토큰 없음");
            validRedisByToken.put("valid", false);
            return validRedisByToken;
        }

        Boolean expired = jwtUtil.isExpired(refreshToken);

        if (expired) { // true : 토큰이 만료됨
            log.warn("리플래시 토큰 만료");
            validRedisByToken.put("expired", false);
            return validRedisByToken;
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        Long id = jwtUtil.getId(refreshToken);
        Long hospital = jwtUtil.getHospital(refreshToken);

        String access = jwtUtil.createJwt("access", id, username, role, hospital, accessExpired);
        validRedisByToken.put("token", access);
        return validRedisByToken;
    }

    public Map<String, Object> saveUserMail(Map<String, Object> authMap) {
        return authRedisService.saveUserMail(authMap);
    }

    public Map<String, Object> getUsersUUIDToken(String token) {
        return authRedisService.getUserUUIDToken(token);
    }
}
