package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "survey_type")
public class SurveyType {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;
}
