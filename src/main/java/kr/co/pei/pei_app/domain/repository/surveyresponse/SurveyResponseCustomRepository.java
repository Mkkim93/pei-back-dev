package kr.co.pei.pei_app.domain.repository.surveyresponse;

import kr.co.pei.pei_app.admin.application.dto.surveys.response.SurveyResponseDetailDTO;
import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;

import java.util.List;

public interface SurveyResponseCustomRepository {

    SurveyResponseFindMetaDTO findMetaData(Long hospitalId);

    List<SurveyResponseDetailDTO> findDetailData(Long surveyId, Long hospitalId, SurveyStatus surveyStatus);
}
