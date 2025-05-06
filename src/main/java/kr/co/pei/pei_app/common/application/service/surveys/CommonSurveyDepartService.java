package kr.co.pei.pei_app.common.application.service.surveys;

import kr.co.pei.pei_app.domain.entity.survey.SurveyDepart;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyDepartJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonSurveyDepartService {

    private final SurveyDepartJpaRepository jpaRepository;

    public List<SurveyDepart> findByHospitalIdByDepartName(Long hospitalId) {
        return jpaRepository.findSurveyDepartByHospitalId(hospitalId);
    }
}
