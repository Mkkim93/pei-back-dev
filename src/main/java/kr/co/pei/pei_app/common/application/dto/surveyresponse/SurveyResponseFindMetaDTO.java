package kr.co.pei.pei_app.common.application.dto.surveyresponse;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.pei.pei_app.common.application.dto.surveys.depart.FindSurveyDepartDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.ward.FindWardDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 환자가 설문 정보 입력을 위한 조회 dto
@Data
@NoArgsConstructor
public class SurveyResponseFindMetaDTO {

    private List<FindWardDTO> wardList;
    private List<FindSurveyDepartDTO> departList;
    private List<String> ageGroup;
    private List<String> genderType;

    @QueryProjection
    public SurveyResponseFindMetaDTO(List<FindWardDTO> wardList, List<FindSurveyDepartDTO> departList, List<String> ageGroup, List<String> genderType) {
        this.wardList = wardList;
        this.departList = departList;
        this.ageGroup = ageGroup;
        this.genderType = genderType;
    }

    public SurveyResponseFindMetaDTO(List<String> ageGroup, List<String> genderType) {
        this.ageGroup = ageGroup;
        this.genderType = genderType;
    }
}
