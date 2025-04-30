package kr.co.pei.pei_app.domain.entity.users;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.hospital.Hospital;
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

    @Column(name = "description")
    private String description;

    @Column(name = "user_img")
    private String userImg;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    // TODO 회원 가입 시 소속 병원 등록 필드
    // 슈퍼 관리자가 소속 병원을 먼저 등록하고 회원가입 가능
    @JoinColumn(name = "hospital_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Hospital hospital;

    public Users(Long id) {
        this.id = id;
    }

    // refresh 토큰에 저장될 사용자 정보 (계정명, 권한)
    public void setJwtPayload(String username, RoleType roleType) {
        this.username = username;
        this.roleType = roleType;
    }

    @Builder
    public Users(Long id, String username, String password, String name, String phone, String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.roleType = RoleType.ROLE_USER; // Default value =  ROLE_USER
        this.createAt = LocalDateTime.now();
        this.phone = phone;
        this.mail = mail;
    }
}
