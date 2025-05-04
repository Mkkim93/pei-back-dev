package kr.co.pei.pei_app.admin.application.dto.hospital;

import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminFindHosDTO {

    private Long id;
    private String name;
    private String description;
    private String imgUrl;

    public AdminFindHosDTO(Long id, String name, String description, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public AdminFindHosDTO toDto(Hospital hospital) {
        this.id = hospital.getId();
        this.name = hospital.getName();
        this.description = hospital.getDescription();;
        this.imgUrl = hospital.getImgUrl();
    }
}
