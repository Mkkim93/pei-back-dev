package kr.co.pei.pei_app.admin.application.dto.hospital;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUpdateHosDTO {

    private Long id;
    private String name;
    private String description;
    private String imgUrl;
}
