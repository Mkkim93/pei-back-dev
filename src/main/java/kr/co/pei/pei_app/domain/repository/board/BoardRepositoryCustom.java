package kr.co.pei.pei_app.domain.repository.board;

import kr.co.pei.pei_app.application.dto.board.DetailBoardDTO;
import kr.co.pei.pei_app.application.dto.board.UpdateBoardDTO;

public interface BoardRepositoryCustom {

    DetailBoardDTO detail(Long boardId);

    Boolean update(UpdateBoardDTO updateBoardDTO);
}
