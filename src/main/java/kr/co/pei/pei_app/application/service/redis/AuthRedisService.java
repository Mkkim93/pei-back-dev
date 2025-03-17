package kr.co.pei.pei_app.application.service.redis;

import kr.co.pei.pei_app.application.exception.users.DuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AuthRedisService : 사용자의 인증번호 및 인증 메일 관련된 캐싱 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthRedisService {

    private final RedisTemplate<String, Object> authRedisTemplate;
    private static final String CODE_PREFIX = "smsCode:";

    public Map<String, Object> getSecretPhoneCode(String phone) {
        log.info("레디스 인증 번호 조회");
        String key = createKey(phone);
        Object code = authRedisTemplate.opsForValue().get(key);

        if (code == null) {
            throw new IllegalArgumentException("인증 번호가 존재하지 않습니다.");
        }

        String codeStr = code.toString();

        Long expire = authRedisTemplate.getExpire(key);

        if (expire == -2L) {
            throw new IllegalArgumentException("인증 번호 입력 시간이 만료 되었습니다. 다시 인증을 시도 해주세요.");
        }

        log.info("인증번호: {}", codeStr);

        Map<String, Object> responseMap = new HashMap<>(2);
        responseMap.put("code", codeStr);
        responseMap.put("expired", expire);

        return responseMap;
    }

    public Long setSmsCode(String phone, String code) {
        log.info("레디스 인증 번호 저장");
        String key = createKey(phone);

        authRedisTemplate.opsForValue().set(key, code, 3L, TimeUnit.MINUTES);
        return authRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public void getUsernameByCode(String phone, String inputCode) {
        log.info("사용자 정보 조회");

        String key = createKey(phone);

        Long expire = authRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        log.info("expire : {}", expire);
        if (expire == -2L) {
            throw new IllegalArgumentException("인증 번호 입력 시간이 만료 되었습니다. 인증번호를 다시 요청해주세요.");
        }

        Object code = authRedisTemplate.opsForValue().get(key);

        if (code == null) {
            throw new IllegalArgumentException("코드가 존재 하지 않습니다.");
        }

        code = code.toString();

        if (!inputCode.equals(code)) {
            throw new IllegalArgumentException("입력하신 인증번호가 존재하지 않습니다.");
        }
    }

    private String createKey(String phone) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("전화번호가 유효하지 않습니다.");
        }

        String normalizedPhone = phone.replaceAll("[^0-9]", "");
        return CODE_PREFIX + normalizedPhone;
    }
}
