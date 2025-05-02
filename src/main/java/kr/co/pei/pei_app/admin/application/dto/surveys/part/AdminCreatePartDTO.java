package kr.co.pei.pei_app.admin.application.dto.surveys.part;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminCreatePartDTO {

    @NotBlank(message = "연령대를 선택해주세요.")
    private String ageGroup;

    @NotBlank(message = "성별을 선택해주세요.")
    private String genderType;

    public AdminCreatePartDTO(String ageGroup, String genderType) {
        this.ageGroup = ageGroup;
        this.genderType = genderType;
    }
}
