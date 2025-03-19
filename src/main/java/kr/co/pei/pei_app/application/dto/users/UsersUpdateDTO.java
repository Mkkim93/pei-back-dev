package kr.co.pei.pei_app.application.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 수정 DTO
 * TODO 정보 수정 컬럼 미정
 */
@Data
@NoArgsConstructor
public class UsersUpdateDTO {

    private String tel;
    private String mail;
}
