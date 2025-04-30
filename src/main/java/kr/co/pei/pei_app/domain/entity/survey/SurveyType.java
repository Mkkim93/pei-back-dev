package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "survey_type")
@Getter
@NoArgsConstructor
public class SurveyType {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Lob
    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_deleted", unique = true)
    private Boolean isDeleted = false;

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
