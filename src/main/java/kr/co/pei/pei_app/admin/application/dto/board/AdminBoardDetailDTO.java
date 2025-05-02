package kr.co.pei.pei_app.admin.application.dto.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pei.pei_app.admin.application.dto.file.AdminDetailFileBoardDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시글 상세 조회를 위한 DTO")
@Data
@NoArgsConstructor
public class AdminBoardDetailDTO {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime updatedAt;
    private Long views;

    @JsonIgnore
    private String username;
    private Long usersId;

    // TODO 나중에 파일 추가
    private List<AdminDetailFileBoardDTO> boardFiles;


    @QueryProjection
    public AdminBoardDetailDTO(Long id, String title, String content, String writer,
                               LocalDateTime updatedAt, Long views, String username, Long usersId,
                               List<AdminDetailFileBoardDTO> boardFiles) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.updatedAt = updatedAt;
        this.views = views;
        this.username = username;
        this.usersId = usersId;
        this.boardFiles = boardFiles;
    }

    @Override
    public String toString() {
        return "DetailBoardDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", updatedAt=" + updatedAt +
                ", views=" + views +
                ", username='" + username + '\'' +
                '}';
    }
}
