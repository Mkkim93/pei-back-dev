package kr.co.pei.pei_app.application.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 등록 DTO
 */
@Schema(description = "회원 가입을 위한 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRegisterDTO {

    @Schema(description = "사용자 계정", example = "king0031443")
    @Size(min = 10, max = 20, message = "숫자와 문자를 포함한 최소 10자리 이상이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "계정 이름은 영문과 숫자만 포함할 수 있습니다.")
    @NotBlank(message = "계정 이름은 필수 입력 사항입니다.")
    private String username;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 문자, 숫자, 특수문자를 포함한 8~20자여야 합니다."
    )
    @Schema(description = "사용자 비밀번호", example = "aa102828@@")
    @Size(min = 8, max = 20, message = "비밀번호는 문자, 숫자, 특수문자를 포함한 8자리 이상이어야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    private String password;

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank(message = "사용자 이름은 필수 입력 사항입니다.")
    private String name;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 '-' 없이 10~11자리 숫자로 입력해야 합니다.")
    @Schema(description = "사용자 전화번호", example = "01055072536")
    @NotBlank(message = "전화번호 입력은 필수 입력 사항입니다.")
    private String phone;

    @Email(message = "올바른 이메일 주소 형식이어야 합니다.")
    @NotBlank(message = "이메일 주소 입력은 필수 입력 사항입니다.")
    private String mail;
}
