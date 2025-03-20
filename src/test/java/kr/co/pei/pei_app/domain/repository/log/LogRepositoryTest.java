package kr.co.pei.pei_app.domain.repository.log;

import kr.co.pei.pei_app.domain.entity.log.Log;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    @Rollback(value = false)
    void addLog() {
            SecurityContextHolder.setContext(new SecurityContextImpl(
                    new UsernamePasswordAuthenticationToken("user4", "1234", List.of(new SimpleGrantedAuthority("ROLE_USER")))
            ));
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(name).get();
        Log log = Log.builder()
                .action("게시글 수정")
//                .description("{title: 게시글 수정 (로그테스트11), boardId: 20, content: 게시글을 수정합니다.}")
                .users(users)
                .build();
        logRepository.save(log);
    }

    @Test
    @DisplayName("유저 로그 기록 조회")
    void findByUsersId() {

        // given
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByUsername(name).get();
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Log> byUsersId = logRepository.findByUsersId(users.getId(), pageRequest);
        List<Log> list = byUsersId.stream().toList();
        for (Log log1 : list) {
            System.out.println("log1.getAction() = " + log1.getAction());
            System.out.println("log1.getDescription() = " + log1.getDescription());
            System.out.println("log1.getUsers().getName() = " + log1.getUsers().getName());
        }
    }
}