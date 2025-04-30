package kr.co.pei.pei_app.domain.entity.survey;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "survey_depart")
@Getter
@NoArgsConstructor
public class SurveyDepart {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "is_deleted", unique = true)
    private boolean isDeleted = false;

    public SurveyDepart(Long id) {
        this.id = id;
    }

    public void setDepartIdAndName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Builder
    public SurveyDepart(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SurveyDepart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
