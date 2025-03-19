package kr.co.pei.pei_app.domain.repository.users;

import kr.co.pei.pei_app.application.dto.users.FindUsersDTO;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class UsersRepositoryTest {

    @Autowired
    private UsersRepository repository;

    @Test
    @DisplayName("모든 사용자 조회")
    void findAllUsers() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<FindUsersDTO> allUsers = repository.findAllUsers(pageRequest);
        allUsers.stream().toList().forEach(System.out::println);
    }

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
    @DisplayName("사용자 메일 조회 테스트")
    void findUsernameByMail() {

        // given
        Users users = Users.builder()
                .username("user8")
                .password("1234")
                .name("관리자8")
                .mail("king00314@naver.com")
                .phone("01055072536")
                .build();

        // when
        repository.save(users);
//        String mail = "king00314@naver.com";
//        String recoverUsername = repository.findUsernameByMail(mail);

//        log.info("가입한 사용자 계정: {}", users.getUsername());
//        log.info("찾은 사용자 계정: {}", recoverUsername);

        // then
//        assertThat(users.getUsername()).isEqualTo(recoverUsername);
    }
}