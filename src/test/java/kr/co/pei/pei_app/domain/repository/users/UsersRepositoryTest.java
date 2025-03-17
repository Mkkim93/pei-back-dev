package kr.co.pei.pei_app.domain.repository.users;

import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Transactional
class UsersRepositoryTest {

    @Autowired
    private UsersRepository repository;

    @Test
    void existsByUsername() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void updateTempPassword() {
    }

    @Test
    void findUsernameByMail() {

        // given
        Users users = Users.builder()
                .username("user1")
                .password("1234")
                .mail("king00314@naver.com")
                .build();

        // when
        repository.save(users);
        String mail = "king00314@naver.com";
        String recoverUsername = repository.findUsernameByMail(mail);

        log.info("가입한 사용자 계정: {}", users.getUsername());
        log.info("찾은 사용자 계정: {}", recoverUsername);

        // then
        assertThat(users.getUsername()).isEqualTo(recoverUsername);
    }
}