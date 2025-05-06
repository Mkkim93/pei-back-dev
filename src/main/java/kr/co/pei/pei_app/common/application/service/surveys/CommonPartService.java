package kr.co.pei.pei_app.common.application.service.surveys;

import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseCreateDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import kr.co.pei.pei_app.domain.entity.survey.enums.AgeGroup;
import kr.co.pei.pei_app.domain.entity.survey.enums.GenderType;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyPartJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonPartService {

    private final SurveyPartJpaRepository jpaRepository;

    public SurveyResponseFindMetaDTO findAllAgeGroupAndGenderType() {

        List<String> ageGroupList = new ArrayList<>();
        List<String> genderTypeList = new ArrayList<>();

        AgeGroup[] ageGroups = AgeGroup.values();
        GenderType[] genderTypes = GenderType.values();

        for (GenderType genderType : genderTypes) {
           genderTypeList.add(genderType.getText());
        }

        for (AgeGroup ageGroup : ageGroups) {
            ageGroupList.add(ageGroup.getText());
        }
        return new SurveyResponseFindMetaDTO(ageGroupList, genderTypeList);
    }

    // 1. 환자 정보 저장
    public SurveyParticipant savePart(SurveyResponseCreateDTO dto, Ward ward) {

        SurveyParticipant entity = SurveyParticipant.builder()
                .ageGroup(AgeGroup.fromText(dto.getAgeGroup()))
                .genderType(GenderType.fromText(dto.getGenderType()))
                .ward(ward)
                .build();

        return jpaRepository.save(entity);
    }
}
