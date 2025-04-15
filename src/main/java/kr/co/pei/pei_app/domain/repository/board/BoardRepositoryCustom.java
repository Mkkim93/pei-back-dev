package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.application.dto.board.BoardDetailDTO;
import kr.co.pei.pei_app.application.dto.board.BoardUpdateDTO;

public interface BoardRepositoryCustom {

    BoardDetailDTO detail(Long boardId);

    Long update(BoardUpdateDTO boardUpdateDTO);
}
