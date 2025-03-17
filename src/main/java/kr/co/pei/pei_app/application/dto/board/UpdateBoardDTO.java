package kr.co.pei.pei_app.application.dto.board;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateBoardDTO {

    private Long boardId;
    private String title;
    private String content;
}
