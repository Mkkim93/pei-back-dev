package kr.co.pei.pei_app.application.service.survey;

import kr.co.pei.pei_app.application.dto.surveys.part.CreatePartDTO;
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
    public void savePart(CreatePartDTO createPartDTO) {

        SurveyParticipant entity = SurveyParticipant.builder()
                .ageGroup(createPartDTO.getAgeGroup())
                .genderType(createPartDTO.getGenderType())
                .build();

        jpaRepository.save(entity);
    }
}
