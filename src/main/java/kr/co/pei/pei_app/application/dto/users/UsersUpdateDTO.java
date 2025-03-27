package kr.co.pei.pei_app.application.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 수정 DTO
 * TODO 정보 수정 컬럼 미정
 */
@Schema(description = "사용자 정보 수정을 위한 DTO")
@Data
@NoArgsConstructor
public class UsersUpdateDTO {
    private String tel;
    private String mail;
}
