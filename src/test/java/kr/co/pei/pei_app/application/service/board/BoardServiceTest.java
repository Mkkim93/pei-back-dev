package kr.co.pei.pei_app.application.service.board;

import kr.co.pei.pei_app.admin.application.dto.board.AdminAdminBoardCreateDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardUpdateDTO;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardDTO;
import kr.co.pei.pei_app.admin.application.service.board.BoardService;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;


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
    @DisplayName("전체 게시글 조회")
    void pages() {
        // given / when
        Page<AdminBoardFindDTO> pages = boardService.pages(PageRequest.of(0, 10), null);

        // then
        for (AdminBoardFindDTO adminBoardFindDTO : pages.getContent()) {
            System.out.println("findBoardDTO.toString() = " + adminBoardFindDTO.toString());
        }
    }

    @Test
    @DisplayName("전체 게시글 조회 (검색)")
    void searchPages() {
        String keyword = "알림";

        Page<AdminBoardFindDTO> pages = boardService.pages(PageRequest.of(0, 10), keyword);

        for (AdminBoardFindDTO dto : pages) {
            System.out.println("dto.toString() = " + dto.toString());
        }
    }

    @Test
    @DisplayName("게시글 상세")
    void detail() {
        // given
        Long boardId = 1L;

        // when
        AdminBoardDetailDTO detail = boardService.detail(boardId);

        // then
        System.out.println("detail.toString() = " + detail.toString());
        assertThat(boardId).isEqualTo(detail.getId());
    }

    @Test
    void update() {
        // given
        Long updateBoardId = 1L;
        String updateTitle = "게시글 제목 수정";
        String updateContent = "게시글 내용 수정";

        // when
        AdminBoardUpdateDTO dto = new AdminBoardUpdateDTO();
        dto.setId(updateBoardId);
        dto.setTitle(updateTitle);
        dto.setContent(updateContent);

        boardService.update(dto);
        Board updatedBoard = boardRepository.findById(updateBoardId).get();

        // then
        assertThat(updateBoardId).isEqualTo(updatedBoard.getId());
        assertThat(updateTitle).isEqualTo(updatedBoard.getTitle());
    }

    @Test
    @DisplayName("게시글 등록")
    @WithMockUser()
    void create() {
        // given
        AdminAdminBoardCreateDTO adminBoardCreateDTO = new AdminAdminBoardCreateDTO();
        adminBoardCreateDTO.setTitle("제목 테스트6");
        adminBoardCreateDTO.setContent("내용 테스트6");

        List<AdminFileBoardDTO> boardFiles = new ArrayList<>();
        AdminFileBoardDTO dto = new AdminFileBoardDTO();
        dto.setName("파일 이름_uuid");
        dto.setOrgName("파일 이름");
        dto.setPath("/");
        dto.setSize(10000L);
        dto.setType("image/png");
        dto.setRenderType("LIST");
        boardFiles.add(dto);
        adminBoardCreateDTO.setBoardFiles(boardFiles);

    }

    @Test
    @DisplayName("게시글 임시 저장")
    void temp() {

    }
}