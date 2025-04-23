package kr.co.pei.pei_app.application.dto.file;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBoardUpdateDTO {

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

