package kr.co.pei.pei_app.admin.application.service.redis;

import kr.co.pei.pei_app.admin.application.exception.redis.PasswordTokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AuthRedisService : 사용자의 인증번호 및 인증 메일 관련된 캐싱 관련 서비스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthRedisService {

    private final RedisTemplate<String, Object> authRedisTemplate;
    private static final String CODE_PREFIX = "smsCode:";
    private static final String MAIL_PREFIX = "reset:token:";

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

    public Map<String, Object> saveUserMail(Map<String, Object> authMap) {
        String uuid = authMap.get("uuid").toString();
        String mailKey = createMailKey(uuid);

        authRedisTemplate.opsForHash().putAll(mailKey, authMap);
        authRedisTemplate.expire(mailKey, Duration.ofMinutes(3)); // 만료 시간 3분
        Map<Object, Object> savedMap = authRedisTemplate.opsForHash().entries(mailKey);

        return parseStringKeyMap(savedMap);
    }

    public Map<String, Object> getUserUUIDToken(String token) {
        log.info("유저가 저장한 토큰 값 조회");
        String mailKey = createMailKey(token);
        Map<Object, Object> savedMap = authRedisTemplate.opsForHash().entries(mailKey);
        if (savedMap.isEmpty()) {
            throw new PasswordTokenExpiredException("입력시간이 만료 되었습니다. 비밀번호를 설정을 다시 시도해주세요");
        }
        return parseStringKeyMap(savedMap);
    }

    public void getUsernameByCode(String phone, String inputCode) {
        log.info("사용자 정보 조회");

        String key = createKey(phone);

        Long expire = authRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        log.info("expire : {}", expire);

        if (expire == -2L) {
            throw new IllegalArgumentException("인증 번호 입력 시간이 만료 되었습니다. 인증번호를 다시 요청해주세요");
        }

        Object code = authRedisTemplate.opsForValue().get(key);

        if (code == null) {
            throw new IllegalArgumentException("인증번호가 존재 하지 않습니다.");
        }

        code = code.toString();

        if (!inputCode.equals(code)) {
            throw new IllegalArgumentException("잘못된 인증번호 입니다 다시 입력해주세요.");
        }
    }

    private String createKey(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("전화번호가 유효하지 않습니다.");
        }
        String normalizedPhone = phone.replaceAll("[^0-9]", "");
        return CODE_PREFIX + normalizedPhone;
    }

    private String createMailKey(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("메일 주소가 유효하지 않습니다.");
        }
        log.info("저장되기 전, 키값 확인 : {} ", MAIL_PREFIX + uuid);
        return MAIL_PREFIX + uuid;
    }

    // key 타입 변경 (Obj -> Str)
    private Map<String, Object> parseStringKeyMap(Map<Object, Object> objectMap) {

        return objectMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        Map.Entry::getValue
                ));
    }
}
