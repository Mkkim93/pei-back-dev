package kr.co.pei.pei_app.application.service.board;

import kr.co.pei.pei_app.application.dto.board.CreateBoardDTO;
import kr.co.pei.pei_app.application.dto.board.FindBoardDTO;
import kr.co.pei.pei_app.domain.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

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

    }
}