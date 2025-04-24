package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.application.dto.board.BoardDetailDTO;
import kr.co.pei.pei_app.application.dto.board.BoardFindDTO;
import kr.co.pei.pei_app.application.dto.board.BoardFindTempDTO;
import kr.co.pei.pei_app.application.dto.board.BoardUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    BoardDetailDTO detail(Long boardId);

    Long update(BoardUpdateDTO boardUpdateDTO);

    Long delete(List<Long> boardId);

    Page<BoardFindDTO> searchPageSimple(String searchKeyword, Pageable pageable);

    Page<BoardFindTempDTO> searchPageTemp(Long usersId, String searchKeyword, Pageable pageable);
}
