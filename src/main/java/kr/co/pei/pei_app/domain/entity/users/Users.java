package kr.co.pei.pei_app.domain.entity.users;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class Users {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mail")
    private String mail;

    @Enumerated(STRING)
    private RoleType roleType;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    // refresh 토큰에 저장될 사용자 정보 (계정명, 권한)
    public void setJwtPayload(String username, RoleType roleType) {
        this.username = username;
        this.roleType = roleType;
    }

    @Builder
    public Users(String username, String password, String name, String phone, String mail) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.roleType = RoleType.ROLE_USER;
        this.createAt = LocalDateTime.now();
        this.phone = phone;
        this.mail = mail;
    }
}
