package kr.co.pei.pei_app.admin.application.service.board;

import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFileSupport;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindTempDTO;
import kr.co.pei.pei_app.admin.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.board.BoardQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardTempService {
    
    private final BoardSaveHelper boardSaveHelper;
    private final UsersContextService usersContextService;
    private final BoardQueryRepository boardQueryRepository;

    public Long savedBoard(AdminBoardFileSupport dto) {
        return boardSaveHelper.boardSaved(dto);
    }

    public Page<AdminBoardFindTempDTO> findPages(Pageable pageable, String searchKeyword) {
        Users users = usersContextService.getCurrentUser();
        return boardQueryRepository.searchPageTemp(users.getId(), searchKeyword, pageable);
    }
}
