package kr.co.pei.pei_app.application.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDownLoadDTO {

    private Long id;
    private String name;
    private String orgName;
    private String path;

    public FileDownLoadDTO(Long id, String name, String orgName, String path) {
        this.id = id;
        this.name = name;
        this.orgName = orgName;
        this.path = path;
    }
}
