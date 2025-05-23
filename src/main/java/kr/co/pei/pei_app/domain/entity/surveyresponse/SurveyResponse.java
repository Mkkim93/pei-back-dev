package kr.co.pei.pei_app.domain.entity.surveyresponse;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
import kr.co.pei.pei_app.domain.entity.survey.Survey;
import kr.co.pei.pei_app.domain.entity.survey.SurveyParticipant;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "survey_response")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SurveyResponse {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(columnDefinition = "json")
    private String answerContent;

    @CreatedDate
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt; // 작성시점

    @Column(name = "response_at")
    private LocalDateTime responseAt; // 응답시점

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participant_id")
    private SurveyParticipant surveyParticipant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @Builder
    public SurveyResponse(String answerContent, LocalDateTime submittedAt, Survey survey, SurveyParticipant surveyParticipant, Ward ward) {
        this.answerContent = answerContent;
        this.submittedAt = submittedAt;
        this.responseAt = LocalDateTime.now();
        this.survey = survey;
        this.surveyParticipant = surveyParticipant;
        this.ward = ward;
    }
}
