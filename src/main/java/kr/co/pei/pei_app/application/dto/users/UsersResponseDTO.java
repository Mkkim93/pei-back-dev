package kr.co.pei.pei_app.application.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 등록된 사용자 응답 DTO
 */
@Schema(description = "회원 가입 성공 시 사용자 정보 반환을 위한 DTO")
@Data
public class UsersResponseDTO {
    private String username;
    private String name;

    public UsersResponseDTO(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
