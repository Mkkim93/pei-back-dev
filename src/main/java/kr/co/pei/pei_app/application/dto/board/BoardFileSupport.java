package kr.co.pei.pei_app.application.dto.board;

import kr.co.pei.pei_app.application.dto.file.FileBoardDTO;

import java.util.List;

public interface BoardFileSupport {
    List<FileBoardDTO> getBoardFiles();
    String getTitle();
    String getContent();
    Boolean getIsTemp();
}
