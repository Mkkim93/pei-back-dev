package kr.co.pei.pei_app.common.application.dto.surveyresponse;

import lombok.Data;
import lombok.NoArgsConstructor;

// 환자가 설문 시작 전 정보 입력 데이터 dto
@Data
@NoArgsConstructor
public class SurveyResponseMetaDTO {

    // 환자 개인 정보
    private Long partId;
    private String ageGroup;
    private String genderType;

    // 진료과 정보
    private Long departId;

    // 병동 정보
    private Long wardId;
}
