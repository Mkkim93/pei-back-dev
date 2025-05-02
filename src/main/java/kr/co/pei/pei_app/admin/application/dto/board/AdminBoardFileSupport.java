package kr.co.pei.pei_app.admin.application.dto.board;

import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardDTO;

import java.util.List;

public interface AdminBoardFileSupport {
    List<AdminFileBoardDTO> getBoardFiles();
    String getTitle();
    String getContent();
    Boolean getIsTemp();
}
