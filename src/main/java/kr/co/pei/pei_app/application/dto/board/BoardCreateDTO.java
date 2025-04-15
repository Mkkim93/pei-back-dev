package kr.co.pei.pei_app.application.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.co.pei.pei_app.application.dto.file.FileBoardDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "게시글 작성 DTO")
@Data
@NoArgsConstructor
public class BoardCreateDTO {

    @NotBlank(message = "제목 입력은 필수 입니다.")
    @Schema(description = "게시글 제목", example = "제목 입니다.")
    private String title;

    @Schema(description = "게시글 내용", example = "내용 입니다.")
    private String content;

    @Schema(description = "게시글 파일", example = "파일 입니다.")
    private List<FileBoardDTO> boardFiles;
}
