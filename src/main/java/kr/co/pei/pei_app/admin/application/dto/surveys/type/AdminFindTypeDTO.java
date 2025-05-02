package kr.co.pei.pei_app.admin.application.dto.surveys.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminFindTypeDTO {

    private Long id;
    private String name;
    private String description;
}
