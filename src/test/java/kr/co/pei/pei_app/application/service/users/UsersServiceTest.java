package kr.co.pei.pei_app.application.service.users;

import kr.co.pei.pei_app.admin.application.service.users.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @Test
    @DisplayName("사용자 계정을 찾기 위해 이메일 입력")
    void recoverUserMail() {
        String mail = "king00313@naver.com";
        usersService.recoverPassword(mail);
    }
}