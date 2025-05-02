package kr.co.pei.pei_app.admin.application.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardUpdateDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "게시글 수정 DTO")
@Data
@NoArgsConstructor
public class AdminBoardUpdateDTO {

    private Long id;
    private String title;
    private String content;
    private List<AdminFileBoardUpdateDTO> boardFiles;
}
