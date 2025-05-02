package kr.co.pei.pei_app.admin.application.service.survey;

import kr.co.pei.pei_app.admin.application.dto.surveys.part.AdminCreatePartDTO;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyPartJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SurveyPartService {

    private final SurveyPartJpaRepository jpaRepository;

    @Transactional
    public void savePart(AdminCreatePartDTO adminCreatePartDTO) {

        SurveyParticipant entity = SurveyParticipant.builder()
                .ageGroup(adminCreatePartDTO.getAgeGroup())
                .genderType(adminCreatePartDTO.getGenderType())
                .build();

        jpaRepository.save(entity);
    }
}
