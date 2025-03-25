package kr.co.pei.pei_app.application.service.redis;

import kr.co.pei.pei_app.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * JwtRedisService : 사용자의 로그인 및 토큰 세션 관리 비즈니스 로직
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtRedisService {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REFRESH_PREFIX = "refresh:";

    // 사용자가 로그인을 시도하면 리플래시 토큰을 레디스에 저장
    public void saveRefreshToken(String username, Object value, long expiredMs) {
        String key = createKey(username);
        log.info("레디스에 로그인 사용자 정보 저장");
        try {
            redisTemplate.opsForValue().set(key, value, expiredMs, TimeUnit.MILLISECONDS);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // 캐싱 데이터에 저장된 사용자의 토큰 만료 시간을 확인하고 만료 시 false, 존재 시 true 반환
    public Boolean getRefreshTokenTTL(String username) {
        log.info("레디스 리플레시 토큰 만료 시간 확인");
        String key = createKey(username);
        Long expire = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);

        if (expire == -1) {
            log.info("토큰 만료 O return : false");
            return false;
        }
        log.info("토큰 만료 X return : true");
        return true;
    }

    // 사용자가 로그아웃을 시도하면 레디스에 저장된 사용자의 리플래시 토큰을 삭제
    public Boolean deleteRefreshToken(String username) {
        log.info("레디스 리플래시 토큰 삭제");
        String key = createKey(username);
        Boolean delete = redisTemplate.delete(key); // key = username

        if (!delete) {
            log.info("DB(REDIS) 토큰 삭제 실패");
            return false;
        }
        log.info("DB(REDIS) 토큰 삭제 성공");
        return true;
    }


    public String findUsernameByToken(String username) {
        log.info("레디스 유저 정보 조회");
        String key = createKey(username);
        Object userToken = redisTemplate.opsForValue().get(key);

        if (userToken == null) {
            throw new AuthenticationException("인증 정보가 만료되었습니다.") {};
        }

        String stringToken = userToken.toString();
        return jwtUtil.getUsername(stringToken);
    }

    private String createKey(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("회원 정보가 유효하지 않습니다.");
        }
        return REFRESH_PREFIX + username;
    }

    public String findRefreshByToken(String token) {
        log.info("레디스 유저 토큰 조회");

        String username = jwtUtil.getUsername(token);
        String key = createKey(username);
        Object tokenStr = redisTemplate.opsForValue().get(key);

        if (tokenStr == null) {
            log.warn("레디스 토큰 만료");
            throw new AuthenticationException("인증이 만료되었습니다.") {};
        }

        return tokenStr.toString();
    }
}