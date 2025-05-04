package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "survey_type")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class SurveyType {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "is_public")
    private boolean isPublic;  // 공통 설문 여부 (true: 공통 / false: 각자 병원 설문)

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Lob
    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_deleted", unique = true)
    private Boolean isDeleted = false;

    @JoinColumn(name = "hospital_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Hospital hospital;

    @Builder
    public SurveyType(String name) {
        this.name = name;
    }

    public SurveyType(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "SurveyType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
