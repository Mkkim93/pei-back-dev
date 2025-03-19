package kr.co.pei.pei_app.application.dto.users;

import lombok.Data;

/**
 * 등록된 사용자 응답 DTO
 */
@Data
public class UsersResponseDTO {
    private String username;
    private String name;

    public UsersResponseDTO(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
