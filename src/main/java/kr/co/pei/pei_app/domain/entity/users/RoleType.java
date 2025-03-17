package kr.co.pei.pei_app.domain.entity.users;

import lombok.Getter;

@Getter
public enum RoleType {

    ROLE_ADMIN("관리자"), ROLE_USER("사용자");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
