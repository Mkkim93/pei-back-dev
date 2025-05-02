package kr.co.pei.pei_app.admin.application.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminFileDownLoadDTO {

    private Long id;
    private String name;
    private String orgName;
    private String path;

    public AdminFileDownLoadDTO(Long id, String name, String orgName, String path) {
        this.id = id;
        this.name = name;
        this.orgName = orgName;
        this.path = path;
    }
}
