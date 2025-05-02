package kr.co.pei.pei_app.admin.application.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminFileBoardDTO {

    private String name;
    private String path;
    private String orgName;
    private String type;
    private Long size;
    private String renderType;
    private boolean used;
    private Long boardId;

    public AdminFileBoardDTO(String name, String path, String orgName, String type,
                             Long size, String renderType, boolean used, Long boardId) {
        this.name = name;
        this.path = path;
        this.orgName = orgName;
        this.type = type;
        this.size = size;
        this.renderType = renderType;
        this.used = used;
        this.boardId = boardId;
    }
}
