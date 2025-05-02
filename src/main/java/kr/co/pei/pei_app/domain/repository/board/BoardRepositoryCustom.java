package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFindTempDTO;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    AdminBoardDetailDTO detail(Long boardId);

    Long update(AdminBoardUpdateDTO adminBoardUpdateDTO);

    Long delete(List<Long> boardId);

    Page<AdminBoardFindDTO> searchPageSimple(String searchKeyword, Pageable pageable);

    Page<AdminBoardFindTempDTO> searchPageTemp(Long usersId, String searchKeyword, Pageable pageable);
}
