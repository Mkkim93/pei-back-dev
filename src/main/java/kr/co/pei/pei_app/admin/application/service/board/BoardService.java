package kr.co.pei.pei_app.admin.application.service.board;

import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFileSupport;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardUpdateDTO;
import kr.co.pei.pei_app.admin.application.exception.board.BoardDeleteFailedException;
import kr.co.pei.pei_app.admin.application.exception.board.BoardNotFoundException;
import kr.co.pei.pei_app.admin.application.service.file.FileStoreService;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.log.AuditLog;
import kr.co.pei.pei_app.domain.entity.notify.NotifyEvent;
import kr.co.pei.pei_app.domain.repository.board.BoardQueryRepository;
import kr.co.pei.pei_app.domain.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardSaveHelper boardSaveHelper;
    private final FileStoreService fileStoreService;
    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private static final String BOARD_NOTIFY_PREFIX = "/detail/";

    @NotifyEvent(message = "새 글이 등록되었습니다", type = "게시글", url = BOARD_NOTIFY_PREFIX)
    @AuditLog(action = "게시글 작성", description = "게시글을 작성 하였습니다.")
    public Long saveBoard(AdminBoardFileSupport dto) {
        return boardSaveHelper.boardSaved(dto);
    }

    public Page<AdminBoardFindDTO> pages(Pageable pageable, String searchKeyword) {
        return boardQueryRepository.searchPageSimple(searchKeyword, pageable);
    }

    public AdminBoardDetailDTO detail(Long boardId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AdminBoardDetailDTO adminBoardDetailDTO = boardQueryRepository.detail(boardId);

        if (adminBoardDetailDTO == null) {
            throw new BoardNotFoundException("게시글이 존재하지 않습니다.");
        }

        if (username.equals(adminBoardDetailDTO.getUsername())) {
            log.info("자신이 작성한 글은 조회 수 증가 X");
            return adminBoardDetailDTO;
        }

        Integer viewCount = boardRepository.updateView(adminBoardDetailDTO.getId());

        if (viewCount < 1) {
            log.info("게시글 조회수 증가 오류: {}", viewCount);
        }
        return adminBoardDetailDTO;
    }

    @NotifyEvent(message = "게시글이 수정되었습니다.", type = "게시글", url = BOARD_NOTIFY_PREFIX) // TODO
    @AuditLog(action = "게시글 업데이트", description = "게시글을 수정하였습니다.")
    public Long update(AdminBoardUpdateDTO adminBoardUpdateDTO) {
        Long updatedBoardId = boardQueryRepository.update(adminBoardUpdateDTO);

        Board board = boardRepository.findById(updatedBoardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        fileStoreService.updateFiles(board, adminBoardUpdateDTO.getBoardFiles());

        return updatedBoardId;
    }

    /**
     * 게시글 삭제 시 논리 삭제로 변경 (isDeleted = 1)
     * @param boardIds
     */
    // TODO 게시글에 정적파일 또는 메타데이터 추가 시 파일 삭제 로직 추가
    @AuditLog(action = "게시글 삭제", description = "게시글을 삭제 하였습니다.")
    public void delete(List<Long> boardIds) {
        long count = boardRepository.countByIdIn(boardIds);

        if (count != boardIds.size()) {
            throw new BoardNotFoundException("삭제 대상 게시글이 존재 하지 않습니다.");
        }

        // 핵심 (게시글 삭제 전, file 메타데이터 + s3 버킷 정리)
        fileStoreService.deleteFilesIfBoardHasFiles(boardIds);

        try {
            boardQueryRepository.delete(boardIds); // 논리 삭제로 변경
        } catch (RuntimeException e) {
            log.warn("게시글 삭제 도중 오류 발생 : {}", e.getMessage());
            throw new BoardDeleteFailedException("게시글 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
