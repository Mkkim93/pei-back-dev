package kr.co.pei.pei_app.admin.application.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminBoardFindTempDTO {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long usersId;

    @QueryProjection
    public AdminBoardFindTempDTO(Long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt, Long usersId) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.usersId = usersId;
    }
}
