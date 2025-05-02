package kr.co.pei.pei_app.admin.application.dto.surveys.survey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

// 양식 업데이트 DTO 객체
@Data
@NoArgsConstructor
public class AdminUpdateSurveyDTO implements AdminSurveyCheckTimeSupport {

    private Long id;
    private String category;
    private String title;
    private Map<String, Object> content;
    private LocalDateTime updatedAt;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private boolean isVisible;
    private Long surveyDepartId;
    private Long surveyTypeId;

    @Override
    public LocalDateTime getOpenAt() {
        return this.openAt;
    }

    @Override
    public LocalDateTime getCloseAt() {
        return this.closeAt;
    }
}
