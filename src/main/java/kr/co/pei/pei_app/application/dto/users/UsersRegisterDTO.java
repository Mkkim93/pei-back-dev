package kr.co.pei.pei_app.application.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRegisterDTO {

    @NotBlank(message = "계정 이름은 필수 입력 사항입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message = "전화번호 입력은 필수 입력 사항입니다.")
    private String phone;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotBlank(message = "메일 주소 입력은 필수 입력 사항입니다.")
    private String mail;
}
