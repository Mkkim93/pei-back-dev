package kr.co.pei.pei_app.application.dto.hospital;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindHosDTO {

    private Long id;
    private String name;

    public FindHosDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
