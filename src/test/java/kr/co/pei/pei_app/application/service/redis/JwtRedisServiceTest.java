package kr.co.pei.pei_app.application.service.redis;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static kr.co.pei.pei_app.domain.entity.users.RoleType.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
class JwtRedisServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void createTable() {
        Users users = new Users();
        users.setJwtPayload("user6", ROLE_ADMIN);
        usersRepository.save(users);

        Users users1 = usersRepository
                .findByUsername("user6")
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));
        System.out.println("users1.getUsername() = " + users1.getUsername());
    }

    @Test
    void saveRefreshToken() {
    }

    @Test
    void getRefreshTokenTTL() {
    }

    @Test
    void deleteRefreshToken() {
    }

    @Test
    @DisplayName("레디스에 저장된 사용자 계정 조회")
    void findUsernameByToken() {
    }
}