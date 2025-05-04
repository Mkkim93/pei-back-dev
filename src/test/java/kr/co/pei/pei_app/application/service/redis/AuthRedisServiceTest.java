package kr.co.pei.pei_app.application.service.redis;

import kr.co.pei.pei_app.admin.application.service.auth.AuthService;
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
class AuthRedisServiceTest {

    @Autowired
    private AuthService authService;

}