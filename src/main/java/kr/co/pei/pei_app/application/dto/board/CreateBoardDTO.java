package kr.co.pei.pei_app.application.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBoardDTO {

    @NotBlank(message = "제목 입력은 필수 입니다.")
    private String title;
    private String content;
}
