package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindDTO;
import kr.co.pei.pei_app.domain.entity.board.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BoardQueryRepositoryTest {

    @Autowired
    private BoardQueryRepository repository;

    @Autowired
    private BoardRepository jpaRepository;

    @Test
    @DisplayName("bulkUpdate isDeleted -> true")
    void bulkUpdate() {
        // given
        List<Long> boardIds = new ArrayList<>();
        boardIds.add(16L);
        boardIds.add(17L);
        boardIds.add(18L);

        // when
        repository.delete(boardIds);
        Board result = jpaRepository.findById(16L).get();

        // then
        assertThat(result.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("게시글 목록 페이징")
    void page() {
        PageRequest page = PageRequest.of(0, 10);
        String searchKeyword = "게시글";
        Page<AdminBoardFindDTO> keywordNull = repository.searchPageSimple(null, page);
        Page<AdminBoardFindDTO> keywordNotNull = repository.searchPageSimple(searchKeyword, page);

        List<AdminBoardFindDTO> result1 = keywordNull.getContent();
        List<AdminBoardFindDTO> result2 = keywordNotNull.getContent();
    }
}