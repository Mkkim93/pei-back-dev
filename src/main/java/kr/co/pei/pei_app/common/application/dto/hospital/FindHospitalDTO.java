package kr.co.pei.pei_app.common.application.dto.hospital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindHospitalDTO {

    private Long id;
    private String name;
    private String description;
    private String imgUrl;
}
