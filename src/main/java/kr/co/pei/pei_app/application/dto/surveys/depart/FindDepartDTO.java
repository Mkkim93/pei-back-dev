package kr.co.pei.pei_app.application.dto.surveys.depart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindDepartDTO {

    private Long id;
    private String name;

    public FindDepartDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
