package kr.co.pei.pei_app.common.application.dto.surveys.survey;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO
@Data
@NoArgsConstructor
public class CommonDetailSurveyDTO {

    private Long id;
    private String title;
    private String category;
    private Object content;

    @QueryProjection
    public CommonDetailSurveyDTO(Long id, String title, String category, Object content) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
