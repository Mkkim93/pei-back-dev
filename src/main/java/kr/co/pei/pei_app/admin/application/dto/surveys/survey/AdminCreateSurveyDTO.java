package kr.co.pei.pei_app.admin.application.dto.surveys.survey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class AdminCreateSurveyDTO implements AdminSurveyCheckTimeSupport {

    private String title;
    private String category;
    private Map<String, Object> content;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private Long surveyTypeId;
    private Long hospitalId;
    private Long surveyDepartId;
    private boolean isVisible;
    private Long usersId; // 양식을 작성한 user

    @Override
    public LocalDateTime getOpenAt() {
        return this.openAt;
    }

    @Override
    public LocalDateTime getCloseAt() {
        return this.closeAt;
    }
}
