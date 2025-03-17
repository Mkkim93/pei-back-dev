package kr.co.pei.pei_app.application.dto.board;

import kr.co.pei.pei_app.domain.entity.board.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FindBoardDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String writer;

    public FindBoardDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createAt = board.getCreatedAt();
        this.updateAt = board.getUpdatedAt();
    }

}
