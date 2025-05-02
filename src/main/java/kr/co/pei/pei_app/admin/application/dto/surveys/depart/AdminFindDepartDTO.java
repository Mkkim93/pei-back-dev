package kr.co.pei.pei_app.admin.application.dto.surveys.depart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminFindDepartDTO {

    private Long id;
    private String name;

    public AdminFindDepartDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
