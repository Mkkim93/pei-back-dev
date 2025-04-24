package kr.co.pei.pei_app.application.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardFindTempDTO {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long usersId;

    @QueryProjection
    public BoardFindTempDTO(Long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt, Long usersId) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.usersId = usersId;
    }
}
