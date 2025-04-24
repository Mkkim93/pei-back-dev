package kr.co.pei.pei_app.application.service.board;

import com.fasterxml.jackson.databind.node.LongNode;
import kr.co.pei.pei_app.application.dto.board.BoardFileSupport;
import kr.co.pei.pei_app.application.dto.board.BoardFindDTO;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.application.service.file.FileStoreService;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.log.AuditLog;
import kr.co.pei.pei_app.domain.entity.notify.NotifyEvent;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class BoardSaveHelper {

    private final UsersContextService contextService;
    private final FileStoreService fileStoreService;
    private final BoardRepository boardRepository;
    private static final String BOARD_NOTIFY_PREFIX = "/detail/";

    @NotifyEvent(message = "새 글이 등록되었습니다", type = "게시글", url = BOARD_NOTIFY_PREFIX)
    @AuditLog(action = "게시글 작성", description = "게시글을 작성 하였습니다.")
    public Long boardSaved(BoardFileSupport dto) {

        Users users = contextService.getCurrentUser();
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .isTemp(dto.getIsTemp())
                .users(users)
                .build();

        Board savedEntity = boardRepository.save(board);

        if (dto.getBoardFiles() != null) {
            fileStoreService.saveFiles(savedEntity, dto);
        }
        return savedEntity.getId();
    }
}
