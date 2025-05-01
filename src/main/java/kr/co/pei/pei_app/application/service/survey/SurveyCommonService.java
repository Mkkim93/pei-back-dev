package kr.co.pei.pei_app.application.service.survey;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.pei.pei_app.application.dto.surveys.survey.CreateSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.DetailSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.FindSurveyDTO;
import kr.co.pei.pei_app.application.dto.surveys.survey.UpdateSurveyDTO;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * SurveyCommonService : Survey 에 속한 세부 도메인 데이터에 접근하기 위해 로그인된 사용자 정보를 추출하기 위함
 * -> UsersContextService(사용자 정보 추출 서비스) 는 항상 여기에서 진행
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyCommonService {

    private final SurveyDepartService departService;
    private final SurveyPartService partService;
    private final SurveyTypeService typeService;
    private final SurveyService surveyService;
    private final UsersContextService contextService; // 사용자에 대한 검증은 현재 서비스에서 모두 진행한다.

    @Transactional
    public void saveSurveyTemplate(CreateSurveyDTO createSurveyDTO) {
        surveyService.save(createSurveyDTO);
    }

    public Map<CategoryType, String> findCategory() {
        return surveyService.findCategory();
    }

    public Page<FindSurveyDTO> findMyPage(Pageable pageable) {
        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        log.info("hospitalId: {} ", hospitalId);
        return surveyService.findSurveyMyPage(pageable, hospitalId);
    }

    @Transactional
    public DetailSurveyDTO findSurveyDetail(Long id) {
        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        return surveyService.findSurveyDetail(id, hospitalId);
    }

    public void surveyUpdate(UpdateSurveyDTO updateSurveyDTO) throws JsonProcessingException {
        surveyService.update(updateSurveyDTO);
    }
}
