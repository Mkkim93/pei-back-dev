package kr.co.pei.pei_app.application.service.board;

import kr.co.pei.pei_app.application.dto.board.CreateBoardDTO;
import kr.co.pei.pei_app.application.dto.board.FindBoardDTO;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.board.BoardRepository;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void loginUser() {
        Users mockUser = Users.builder()
                .id(1L)
                .username("user1")
                .name("관리자1")
                .password("1234")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void pages() {

    }

    @Test
    void detail() {
    }

    @Test
    void update() {
    }

    @Test
    @DisplayName("게시글 등록")
    void create() {
        // given
        CreateBoardDTO createBoardDTO = new CreateBoardDTO();
        createBoardDTO.setTitle("제목 테스트1");
        createBoardDTO.setContent("내용 테스트1");
        boardService.create(createBoardDTO);
    }
}