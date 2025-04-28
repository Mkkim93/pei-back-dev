package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.survey.enums.AgeGroup;
import kr.co.pei.pei_app.domain.entity.survey.enums.GenderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "survey_participant")
@NoArgsConstructor
public class SurveyParticipant {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    @Enumerated(STRING)
    private AgeGroup ageGroup;

    @Column
    @Enumerated(STRING)
    private GenderType genderType;

    @Builder
    public SurveyParticipant(String ageGroup, String genderType) {
        this.ageGroup = AgeGroup.valueOf(ageGroup);
        this.genderType = GenderType.valueOf(genderType);
    }
}
