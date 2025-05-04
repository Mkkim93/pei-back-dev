package kr.co.pei.pei_app.domain.repository.survey.jpa;

import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminTypeStatusDTO;
import kr.co.pei.pei_app.admin.application.dto.surveys.type.AdminTypeUsageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SurveyTypeRepositoryCustom {

    Page<AdminTypeUsageDTO> findUsagePage(Pageable pageable, Long hospitalId, boolean isPublic);

    Page<AdminTypeStatusDTO> findStatusPage(Pageable pageable, Long hospitalId, boolean isPublic, String surveyStatus);
}
