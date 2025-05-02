package kr.co.pei.pei_app.admin.application.dto.surveys.survey;

import java.time.LocalDateTime;

public interface AdminSurveyCheckTimeSupport {
    LocalDateTime getOpenAt();
    LocalDateTime getCloseAt();
}
