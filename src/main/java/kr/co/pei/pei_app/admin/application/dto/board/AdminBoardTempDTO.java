package kr.co.pei.pei_app.admin.application.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO 나중에 dto 네이밍 변경 (작성 하는 건지 알수있게)
@Data
@Schema
@NoArgsConstructor
public class AdminBoardTempDTO implements AdminBoardFileSupport {

    private Long id;
    private String title;
    private String content;
    private Boolean isTemp; // 임시 저장 여부
    private List<AdminFileBoardDTO> boardFiles;

    @Override
    public List<AdminFileBoardDTO> getBoardFiles() {
        return this.boardFiles;
    }

    @Override
    public Boolean getIsTemp() {
        return this.isTemp;
    }
}
