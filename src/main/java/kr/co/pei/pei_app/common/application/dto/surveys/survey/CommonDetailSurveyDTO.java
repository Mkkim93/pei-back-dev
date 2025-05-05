package kr.co.pei.pei_app.common.application.dto.surveys.survey;

import com.fasterxml.jackson.annotation.JsonFormat;
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
}
