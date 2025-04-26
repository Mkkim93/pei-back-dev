package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Survey {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private CategoryType category;

    @Column(columnDefinition = "text")
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private SurveyType surveyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depart_id")
    private SurveyDepart surveyDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

}
