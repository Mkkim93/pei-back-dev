package kr.co.pei.pei_app.application.service.board;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.application.dto.board.CreateBoardDTO;
import kr.co.pei.pei_app.application.dto.board.DetailBoardDTO;
import kr.co.pei.pei_app.application.dto.board.FindBoardDTO;
import kr.co.pei.pei_app.application.dto.board.UpdateBoardDTO;
import kr.co.pei.pei_app.application.exception.board.BoardDeleteFailedException;
import kr.co.pei.pei_app.application.exception.board.BoardNotFoundException;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.log.AuditLog;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.board.BoardQueryRepository;
import kr.co.pei.pei_app.domain.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UsersContextService contextService;
    private final BoardQueryRepository boardQueryRepository;

    @AuditLog(action = "게시글 작성", description = "게시글을 작성 하였습니다.")
    public Long create(CreateBoardDTO createBoardDTO) {

        Users users = contextService.getCurrentUser();

        Board board = Board.builder()
                .title(createBoardDTO.getTitle())
                .content(createBoardDTO.getContent())
                .users(users)
                .build();

        Board save = boardRepository.save(board);

        return save.getId();
    }

    public Page<FindBoardDTO> pages(Pageable pageable, String searchKeyword) {
        if (searchKeyword != null) {
            // TODO
        }
        Page<Board> boardPages = boardRepository.findAll(pageable);
        return boardPages.map(FindBoardDTO::new);
    }

    public DetailBoardDTO detail(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Users users = contextService.getCurrentUser();
        DetailBoardDTO detailBoardDTO = boardQueryRepository.detail(board.getId());

        if (users.getUsername().equals(detailBoardDTO.getUsername())) {
            log.info("자신이 작성한 글은 조회 수 증가 X");
            return detailBoardDTO;
        }

        Integer viewCount = boardRepository.updateView(board.getId());

        if (viewCount < 1) {
            log.info("게시글 조회수 증가 오류: {}", viewCount);
        }
        return detailBoardDTO;
    }

    @AuditLog(action = "게시글 업데이트")
    public Boolean update(UpdateBoardDTO updateBoardDTO) {
        Boolean update = boardQueryRepository.update(updateBoardDTO);
        if (!update) {
            return false;
        }
        return true;
    }

    // TODO 게시글에 정적파일 또는 메타데이터 추가 시 파일 삭제 로직 추가
    @AuditLog(action = "게시글 삭제", description = "게시글을 삭제 하였습니다.")
    public void delete(List<Long> boardIds) {
        long count = boardRepository.countByIdIn(boardIds);

        if (count != boardIds.size()) {
            throw new BoardNotFoundException("삭제 대상 게시글이 존재 하지 않습니다.");
        }

        try {

            boardRepository.deleteAllById(boardIds);

        } catch (Exception e) {
            throw new BoardDeleteFailedException("게시글 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
