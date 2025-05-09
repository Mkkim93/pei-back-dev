package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import kr.co.pei.pei_app.admin.application.dto.surveys.survey.AdminUpdateSurveyDTO;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import kr.co.pei.pei_app.domain.entity.survey.enums.CategoryType;
import kr.co.pei.pei_app.domain.entity.survey.enums.SurveyStatus;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Survey {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String title; // 설문 메인 타이틀: ex) 2025년도 00대학병원 환자 만족도 평가

    @Enumerated(STRING)
    private CategoryType category; // 서브 카테고리: ex) 환자 만족도 설문 조사, 온라인 상담 만족도 설문 조사

    @Column(columnDefinition = "json")
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 설문 생성일

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 설문 수정일(최종)

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "open_at")
    private LocalDateTime openAt; // 설문 시작일 (boolean -> Boolean) 05/06

    @Column(name = "close_at")
    private LocalDateTime closeAt; // 설문 종료일

    @Enumerated(STRING)
    @Column(name = "survey_status")
    private SurveyStatus surveyStatus; // 설문 상태 (대기, 진행중, 종료)

    @Column(name = "is_visible") // 설문 공개/비공개 (true : 공개, false : 비공개)
    private boolean isVisible;

    @Column(name = "expected_part")
    private Long expectedParticipants; // 설문 참여자 수 추정 (실제 설문이 종료되면 이 값으로 전체 참여자 추정 수와 실제 참여한 참여자 수를 측정하여 응답률 통계)

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_id")
    private SurveyType surveyType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "depart_id")
    private SurveyDepart surveyDepart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    public void updateForm(AdminUpdateSurveyDTO dto,
                           CategoryType category, String jsonContent, SurveyType surveyType,
                           SurveyDepart surveyDepart, SurveyStatus surveyStatus) {

        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getCategory() != null) this.category = category;
        if (dto.getContent() != null) this.content = jsonContent;

        this.openAt = dto.getOpenAt() != null ? dto.getOpenAt() : this.openAt;
        this.closeAt = dto.getCloseAt() != null ? dto.getCloseAt() : this.closeAt;

        if (dto.getUpdatedAt() != null) this.updatedAt = dto.getUpdatedAt();
        if (dto.getSurveyDepartId() != null) this.surveyDepart = surveyDepart;
        if (dto.getSurveyTypeId() != null) this.surveyType = surveyType;

        this.surveyStatus = surveyStatus;
        this.isVisible = dto.isVisible();
    }

    @Builder
    public Survey(Long id, String title, CategoryType category, String content,
                  LocalDateTime openAt, LocalDateTime closeAt, LocalDateTime updatedAt, SurveyStatus surveyStatus,
                  boolean isVisible, SurveyType type, SurveyDepart depart, Hospital hospital, Users users) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.content = content;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.surveyStatus = surveyStatus;
        this.isVisible = isVisible;
        this.surveyType = type;
        this.surveyDepart = depart;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = updatedAt;
        this.hospital = hospital;
        this.users = users;
    }
}
