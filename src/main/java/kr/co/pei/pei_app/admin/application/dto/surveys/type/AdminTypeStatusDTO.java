package kr.co.pei.pei_app.admin.application.dto.surveys.type;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminTypeStatusDTO {

    // SurveyType
    private Long id;
    private String name;
    private String description;

    // Survey
    private String title;
    private String category;
    private String surveyStatus;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private boolean isVisible;

    private String departName;
    private String hospitalName;

    @QueryProjection
    public AdminTypeStatusDTO(Long id, String name, String description,
                              String title, String category, String surveyStatus,
                              LocalDateTime openAt, LocalDateTime closeAt, LocalDateTime createdAt,
                              LocalDateTime deletedAt, LocalDateTime updatedAt, boolean isVisible,
                              String departName, String hospitalName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.title = title;
        this.category = category;
        this.surveyStatus = surveyStatus;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.isVisible = isVisible;
        this.departName = departName;
        this.hospitalName = hospitalName;
    }
}
