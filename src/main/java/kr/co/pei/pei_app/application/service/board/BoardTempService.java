package kr.co.pei.pei_app.application.service.board;

import kr.co.pei.pei_app.application.dto.board.BoardFileSupport;
import kr.co.pei.pei_app.application.dto.board.BoardFindTempDTO;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.board.BoardQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardTempService {
    
    private final BoardSaveHelper boardSaveHelper;
    private final UsersContextService usersContextService;
    private final BoardQueryRepository boardQueryRepository;

    public Long savedBoard(BoardFileSupport dto) {
        return boardSaveHelper.boardSaved(dto);
    }

    public Page<BoardFindTempDTO> findPages(Pageable pageable, String searchKeyword) {
        Users users = usersContextService.getCurrentUser();
        return boardQueryRepository.searchPageTemp(users.getId(), searchKeyword, pageable);
    }
}
