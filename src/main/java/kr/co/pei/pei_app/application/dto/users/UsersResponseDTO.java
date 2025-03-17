package kr.co.pei.pei_app.application.dto.users;

import lombok.Data;

@Data
public class UsersResponseDTO {
    private String username;
    private String name;

    public UsersResponseDTO(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
