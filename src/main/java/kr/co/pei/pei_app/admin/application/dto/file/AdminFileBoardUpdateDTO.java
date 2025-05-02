package kr.co.pei.pei_app.admin.application.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminFileBoardUpdateDTO {

    private Long id;
    private String name;
    private String path;
    private String orgName;
    private String type;
    private Long size;
    private String renderType;
    private boolean used;
    private Long boardId;
}

