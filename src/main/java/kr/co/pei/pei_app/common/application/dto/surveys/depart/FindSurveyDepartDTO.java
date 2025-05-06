package kr.co.pei.pei_app.common.application.dto.surveys.depart;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindSurveyDepartDTO {

    private Long id;
    private String name;

    public FindSurveyDepartDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
