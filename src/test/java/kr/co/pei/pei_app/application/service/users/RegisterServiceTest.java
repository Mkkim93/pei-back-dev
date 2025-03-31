package kr.co.pei.pei_app.application.service.users;

import kr.co.pei.pei_app.application.dto.users.UsersRegisterDTO;
import kr.co.pei.pei_app.application.dto.users.UsersResponseDTO;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
//@Transactional
@ActiveProfiles("test")
@SpringBootTest
class RegisterServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private RegisterService registerService;

    @Test
    @DisplayName("최종 회원 가입")
    void register() {

        // given
        UsersRegisterDTO dto = new UsersRegisterDTO();
        dto.setUsername("user1");
        dto.setPassword("1234");
        dto.setName("관리자1");
        dto.setMail("king00314@naver.com");
        dto.setPhone("01055072536");

        // when
        UsersResponseDTO register = registerService.register(dto);

        // then
        assertThat(dto.getUsername()).isEqualTo(register.getUsername());

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