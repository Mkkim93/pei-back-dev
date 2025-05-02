package kr.co.pei.pei_app.admin.application.service.survey;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminCreateSurveyDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminFindSurveyDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminSurveyDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminUpdateSurveyDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminFindTypeDTO;
import kr.co.pei.pei_app.admin.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.entity.survey.SurveyType;
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
    public void saveSurveyTemplate(AdminCreateSurveyDTO adminCreateSurveyDTO) {
        surveyService.save(adminCreateSurveyDTO);
    }

    public Map<CategoryType, String> findCategory() {
        return surveyService.findCategory();
    }

    public Page<AdminFindSurveyDTO> findMyPage(Pageable pageable) {
        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        log.info("hospitalId: {} ", hospitalId);
        return surveyService.findSurveyMyPage(pageable, hospitalId);
    }

    @Transactional
    public AdminSurveyDetailDTO findSurveyDetail(Long id) {
        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        return surveyService.findSurveyDetail(id, hospitalId);
    }

    @Transactional
    public void surveyUpdate(AdminUpdateSurveyDTO adminUpdateSurveyDTO) throws JsonProcessingException {
        SurveyType type = typeService.findById(adminUpdateSurveyDTO.getSurveyTypeId());
        SurveyDepart depart = departService.findById(adminUpdateSurveyDTO.getSurveyDepartId());
        surveyService.update(adminUpdateSurveyDTO, type, depart);
    }

    @Transactional(readOnly = true)
    public Page<AdminFindTypeDTO> findMyHosTypeSurveyPage(Pageable pageable, boolean isPublic) {

        if (isPublic) {
            return typeService.findAllPages(pageable);
        }

        Long hospitalId = contextService.getCurrentUser().getHospital().getId();
        return typeService.findPages(pageable, hospitalId);
    }
}
