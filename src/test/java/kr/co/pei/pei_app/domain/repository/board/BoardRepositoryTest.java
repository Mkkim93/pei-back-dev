package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.application.dto.board.BoardFindDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 제목 + 내용 검색")
    void searchByBoardPages() {
        // given
        String searchKeyword = "테스트";
        Page<BoardFindDTO> boards = boardRepository
                .searchByBoardPages(searchKeyword, PageRequest.of(0, 10));

        // when
        List<BoardFindDTO> content = boards.getContent();
        for (BoardFindDTO board : content) {
            System.out.println("board.getId() = " + board.getId());
            System.out.println("board.getTitle() = " + board.getTitle());
            System.out.println("board.getContent() = " + board.getContent());
        }

        // then
        content.forEach(board -> {
            boolean matches =
                    (board.getTitle() != null && board.getTitle().contains(searchKeyword)) ||
                            (board.getContent() != null && board.getContent().contains(searchKeyword));
            assertTrue(matches, "검색 키워드가 title 또는 content 에 포함되어야 합니다.");
        });
    }
}