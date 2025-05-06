package kr.co.pei.pei_app.common.application.dto.surveys.ward;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindWardDTO {

    private Long id;
    private String name;

    public FindWardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
