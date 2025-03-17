package kr.co.pei.pei_app.application.service.users;

import kr.co.pei.pei_app.application.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class RegisterServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("최종 회원 가입")
    void register() {
    }

    @Test
    @DisplayName("인증 번호 요청")
    void requestCode() {
    }

    @Test
    @DisplayName("인증 번호 검증")
    void validCode() {
    }

    @Test
    @DisplayName("계정 중복 확인")
    void existByUsername() {
    }

    @Test
    @DisplayName("비밀번호 강도 검증")
    void checkPasswordStrength() {
    }
}