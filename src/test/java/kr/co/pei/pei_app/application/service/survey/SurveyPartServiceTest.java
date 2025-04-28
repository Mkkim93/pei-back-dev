package kr.co.pei.pei_app.application.service.survey;

import kr.co.pei.pei_app.application.dto.survey.part.CreatePartDTO;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import kr.co.pei.pei_app.domain.entity.survey.enums.AgeGroup;
import kr.co.pei.pei_app.domain.entity.survey.enums.GenderType;
import kr.co.pei.pei_app.domain.repository.survey.jpa.SurveyPartJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class SurveyPartServiceTest {

    @Autowired
    private SurveyPartService service;

    @Autowired
    private SurveyPartJpaRepository jpaRepository;

    @Test
    @DisplayName("설문자 정보 등록")
    void createPart() {
        // given
        CreatePartDTO dto = new CreatePartDTO();

        dto.setAgeGroup("AGE_20S");
        dto.setGenderType("MALE");

        // when
        service.savePart(dto);

        // then
        List<SurveyParticipant> result = jpaRepository.findAll();

        assertThat(result).hasSize(1);

        SurveyParticipant participant = result.get(0);

        assertThat(participant.getAgeGroup()).isEqualTo(AgeGroup.AGE_20S);
        assertThat(participant.getGenderType()).isEqualTo(GenderType.MALE);
    }
}