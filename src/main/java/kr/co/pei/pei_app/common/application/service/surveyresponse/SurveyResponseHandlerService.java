package kr.co.pei.pei_app.common.application.service.surveyresponse;

import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseCreateDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.depart.FindSurveyDepartDTO;
import kr.co.pei.pei_app.common.application.dto.surveys.ward.FindWardDTO;
import kr.co.pei.pei_app.common.application.service.hospital.CommonHospitalService;
import kr.co.pei.pei_app.common.application.service.surveys.*;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SurveyResponseHandlerService {

    private final CommonSurveyService surveyService;
    private final CommonHospitalService hospitalService;
    private final CommonWardService wardService;
    private final CommonSurveyDepartService departService;
    private final CommonPartService partService;
    private final CommonSurveyResponseService responseService;

    // 설문 초기 정보 입력 시 진료과, 병동 리스트 조회를 위한 메서드
    public SurveyResponseFindMetaDTO findMetaList(Long hospitalId) {
        // 1. 병동 리스트 조회
        List<Ward> wardList = wardService.findByHospitalIdByWardName(hospitalId);

        List<FindWardDTO> toWardDTO = wardList.stream().map(ward -> new FindWardDTO(
                ward.getId(),
                ward.getName()
        )).toList();

        // 2. 진료과 리스트 조회
        List<SurveyDepart> departList = departService.findByHospitalIdByDepartName(hospitalId);

        List<FindSurveyDepartDTO> toDepartDTO = departList.stream().map(surveyDepart -> new FindSurveyDepartDTO(
                surveyDepart.getId(),
                surveyDepart.getName()
        )).toList();

        SurveyResponseFindMetaDTO dto = partService.findAllAgeGroupAndGenderType();
        dto.setDepartList(toDepartDTO);
        dto.setWardList(toWardDTO);

        return dto;
    }

    public void saveHandler(SurveyResponseCreateDTO dto) {
        // 1. 병동 엔티티 조회
        Ward ward = wardService.findByWardId(dto.getWardId());

        // 2. 설문 양식 엔티티 조회
        Survey survey = surveyService.findBySurveyId(dto.getSurveyId());

        // 3. 환자 정보 저장
        SurveyParticipant part = partService.savePart(dto, ward);

        // 4. 최종 응답 저장
        responseService.saveResponse(dto, survey, ward, part);
    }
}
