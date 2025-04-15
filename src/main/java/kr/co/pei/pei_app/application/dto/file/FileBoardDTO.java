package kr.co.pei.pei_app.application.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileBoardDTO {

    private String name;
    private String path;
    private String orgName;
    private String type;
    private Long size;
    private Long boardId;
}
