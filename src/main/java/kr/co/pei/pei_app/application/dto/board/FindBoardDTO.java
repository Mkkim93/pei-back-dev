package kr.co.pei.pei_app.application.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pei.pei_app.domain.entity.board.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 전체 게시글 조회 DTO
 */
@Schema(description = "전체 게시글 조회 DTO")
@Data
@NoArgsConstructor
public class FindBoardDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String writer;
    private Long views;

    public FindBoardDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createAt = board.getCreatedAt();
        this.updateAt = board.getUpdatedAt();
        this.writer = board.getUsers().getName();
        this.views = board.getViews();
    }
}
