package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.survey.enums.GenderType;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "survey_participant")
public class SurveyParticipant {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Long age;

    @Column
    @Enumerated(STRING)
    private GenderType genderType;
}
