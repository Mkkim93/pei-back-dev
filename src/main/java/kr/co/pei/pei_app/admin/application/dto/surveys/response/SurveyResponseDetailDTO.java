package kr.co.pei.pei_app.admin.application.dto.surveys.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name = "차트로 가공할 설문 응답 데이터 객체",
        description = "surveyId 를 기준으로 응답 데이터를 List<DTO> 로 반환.")
@Data
@NoArgsConstructor
public class SurveyResponseDetailDTO {

    // survey response
    @Schema(name = "설문응답번호")
    private Long id;

    @Schema(name = "설문주제")
    private String title;

    @Schema(name = "설문유형")
    private String category;

    @Schema(name = "설문양식")
    private String content;

    @Schema(name = "설문응답")
    private String answerContent;

    @Schema(name = "설문번호")
    private Long surveyId;

    @Schema(name = "설문병원")
    private Long hospitalId;

    @Schema(name = "설문참여자정보")
    private Long surveyPartId;

    @Schema(name = "진료과")
    private Long wardId;

    @QueryProjection
    public SurveyResponseDetailDTO(Long id, String title, String category, String answerContent, Long surveyId, Long hospitalId, Long surveyPartId, Long wardId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.answerContent = answerContent;
        this.surveyId = surveyId;
        this.hospitalId = hospitalId;
        this.surveyPartId = surveyPartId;
        this.wardId = wardId;
    }

    @Override
    public String toString() {
        return "SurveyResponseDetailDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", surveyId=" + surveyId +
                ", hospitalId=" + hospitalId +
                ", surveyPartId=" + surveyPartId +
                ", wardId=" + wardId +
                '}';
    }
}
