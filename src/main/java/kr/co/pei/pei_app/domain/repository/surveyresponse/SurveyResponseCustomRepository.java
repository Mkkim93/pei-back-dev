package kr.co.pei.pei_app.domain.repository.surveyresponse;

import kr.co.pei.pei_app.common.application.dto.surveyresponse.SurveyResponseFindMetaDTO;

public interface SurveyResponseCustomRepository {

    SurveyResponseFindMetaDTO findMetaData(Long hospitalId);
}
