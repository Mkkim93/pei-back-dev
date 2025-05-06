package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.hospital.Ward;
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
    private AgeGroup ageGroup; // 환자 연령대

    @Column
    @Enumerated(STRING)
    private GenderType genderType; // 환자 성별

    @JoinColumn(name = "ward_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ward ward; // 환자 설문 초기 화면에서 병동 정보 입력 시 필수

    @Builder
    public SurveyParticipant(AgeGroup ageGroup, GenderType genderType, Ward ward) {
        this.ageGroup = ageGroup;
        this.genderType = genderType;
        this.ward = ward;
    }
}
