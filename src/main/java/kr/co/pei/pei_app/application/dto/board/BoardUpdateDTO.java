package kr.co.pei.pei_app.application.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 수정 DTO")
@Data
@NoArgsConstructor
public class BoardUpdateDTO {

    private Long id;
    private String title;
    private String content;
}
