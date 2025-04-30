package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

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
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participant_id")
    private SurveyParticipant surveyParticipant;

    @Builder
    public SurveyResponse(String answerContent, Survey survey, SurveyParticipant surveyParticipant) {
        this.answerContent = answerContent;
        this.survey = survey;
        this.submittedAt = LocalDateTime.now();
        this.surveyParticipant = surveyParticipant;
    }
}
