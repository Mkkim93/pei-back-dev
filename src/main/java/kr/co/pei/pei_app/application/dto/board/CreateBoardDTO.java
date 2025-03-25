package kr.co.pei.pei_app.application.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 작성 DTO")
@Data
@NoArgsConstructor
public class CreateBoardDTO {

    @NotBlank(message = "제목 입력은 필수 입니다.")
    private String title;
    private String content;
}
