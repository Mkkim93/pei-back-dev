package kr.co.pei.pei_app.admin.application.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.users.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 전체 게시글 조회 DTO
 */
@Schema(description = "전체 게시글 조회 DTO")
@Data
@NoArgsConstructor
public class AdminBoardFindDTO {

    private Long id;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String writer;
    private Long views;
    private String roleType;
    private Long usersId;


    public AdminBoardFindDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.createAt = board.getCreatedAt();
        this.updateAt = board.getUpdatedAt();
        this.writer = board.getUsers().getName();
        this.roleType = board.getUsers().getRoleType().getText();
        this.views = board.getViews();
        this.usersId = board.getUsers().getId();
    }

    @QueryProjection
    public AdminBoardFindDTO(Long id, String title,
                             LocalDateTime createAt, LocalDateTime updateAt, String writer, RoleType roleType,
                             Long views, Long usersId) {
        this.id = id;
        this.title = title;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.writer = writer;
        this.roleType = roleType.getText();
        this.views = views;
        this.usersId = usersId;
    }

    @Override
    public String toString() {
        return String.format(
                "FindBoardDTO{id=%d, title='%s', content='%s', writer='%s', updateAt=%s, views=%d}",
                id, title, writer, updateAt, views
        );
    }
}
