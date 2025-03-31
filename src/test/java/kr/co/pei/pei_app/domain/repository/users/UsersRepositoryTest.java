package kr.co.pei.pei_app.domain.repository.users;

import kr.co.pei.pei_app.application.dto.users.FindUsersDTO;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UsersRepositoryTest {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void loginUser() {
        Users mockUser = Users.builder()
                .username("user1")
                .password("1234")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("모든 사용자 조회")
    void findAllUsers() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<FindUsersDTO> allUsers = repository.findAllUsers(pageRequest);

        // then
        List<FindUsersDTO> content = allUsers.getContent();

        for (FindUsersDTO dto : content) {
            System.out.println("dto.getUsername() = " + dto.getUsername());
            System.out.println("dto.getName() = " + dto.getName());
            System.out.println("dto.getRoleType() = " + dto.getRoleType());
        }
    }

    @Test
    @DisplayName("사용자 계정 중복 검증 (True / False)")
    void existsByUsername() {
        // given
        String username1 = "user1";
        String username2 = "user2";

        // when
        boolean existsTrue = repository.existsByUsername(username1);
        boolean existsFalse = repository.existsByUsername(username2);

        // then
        Assertions.assertThat(existsTrue).isTrue();
        Assertions.assertThat(existsFalse).isFalse();
    }

    @Test
    @DisplayName("사용자 계정 중복 검증 (Optional 조회)")
    void findByUsername() {
        // given
        String username1 = "user1";
        String username2 = "user2";

        // when
        Optional<Users> users1 = repository.findByUsername(username1);
        Optional<Users> users2 = repository.findByUsername(username2);

        // then
        Assertions.assertThat(users1.get().getUsername()).isEqualTo(username1);
        Assertions.assertThat(users2).isEmpty();
    }

    @Test
    // TODO 비즈니스 로직 미구현
    void updateTempPassword() {
        // given
        String password = "4321";
        String username = "user1";

        String encodedPassword = encoder.encode(password);

        // when
        repository.updateTempPassword(encodedPassword, username);

        Optional<Users> users = repository.findByUsername(username);
        String encodePassword = users.get().getPassword();

        // then
        Assertions.assertThat(users.get().getUsername()).isEqualTo(username);
        assertThat(encoder.matches(password, encodePassword)).isTrue();

    }

    @Test
    @DisplayName("사용자 메일 조회 테스트")
    void findUsernameByMail() {

        // given
        String username = "user1";
        String mail = "king00314@naver.com";

        // when
        String recoverUsername = repository.findUsernameByMail(mail);

        log.info("가입한 사용자 계정: {}", username);
        log.info("찾은 사용자 계정: {}", recoverUsername);

        // then
        assertThat(username).isEqualTo(recoverUsername);
    }
}