package kr.co.pei.pei_app.admin.application.dto.file;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.pei.pei_app.domain.entity.file.RenderType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminDetailFileBoardDTO {

    private Long id;
    private String name;
    private String path;
    private String orgName;
    private Long boardId;
    private RenderType renderType;
    private boolean used;

    @QueryProjection
    public AdminDetailFileBoardDTO(Long id, String name, String path, String orgName, Long boardId,
                                   RenderType renderType, boolean used) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.orgName = orgName;
        this.boardId = boardId;
        this.renderType = renderType;
        this.used = used;
    }
}
