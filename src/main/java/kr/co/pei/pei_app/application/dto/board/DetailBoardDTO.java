package kr.co.pei.pei_app.application.dto.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DetailBoardDTO {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime updatedAt;
    private Long views;

    @JsonIgnore
    private String username;
    // TODO 나중에 파일 추가

    @QueryProjection
    public DetailBoardDTO(Long id, String title, String content,
                          String writer, LocalDateTime updatedAt, Long views, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.updatedAt = updatedAt;
        this.views = views;
        this.username = username;
    }
}
