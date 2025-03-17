package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pei.pei_app.application.dto.api.ApiResponse;
import kr.co.pei.pei_app.application.dto.api.PasswordCheckResponse;
import kr.co.pei.pei_app.application.dto.users.UsersResponseDTO;
import kr.co.pei.pei_app.application.dto.users.UsersRegisterDTO;
import kr.co.pei.pei_app.application.service.users.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REGISTER_API")
@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    @Operation(summary = "최종 회원가입", description = "모든 인증 완료 후 회원 가입 요청을 위한 API")
    public ResponseEntity<ApiResponse<UsersResponseDTO>> register(@Valid @RequestBody UsersRegisterDTO registerDTO) {
        UsersResponseDTO registerUsers = registerService.register(registerDTO);
        return ResponseEntity.ok(ApiResponse.success("가입이 완료 되었습니다.", registerUsers));
    }

    @GetMapping("/check-username")
    @Operation(summary = "계정 중복 확인", description = "회원 가입 시 입력한 계정 중복 확인")
    public ResponseEntity<ApiResponse<String>> existUsername(@RequestParam("username") String username) {
        boolean existByUsername = registerService.existByUsername(username);

        if (existByUsername) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), "EXIST_USERNAME", "사용 중인 계정 입니다."));
        }
        return ResponseEntity.ok(ApiResponse.success("사용 가능한 계정입니다.", username));
    }

    @PostMapping("/password-strength")
    @Operation(summary = "비밀번호 강도 체크", description = "사용자가 입력한 비밀번호가 충분히 안전한지 확인")
    public ResponseEntity<ApiResponse<PasswordCheckResponse>> checkPasswordStrength(@RequestParam("password") String password) {

        PasswordCheckResponse passwordCheckResponse = registerService.checkPasswordStrength(password);

        if (passwordCheckResponse.getGrade() > 3) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("안전한 비밀번호 입니다.", passwordCheckResponse));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(),
                "BAD_PASSWORD","안전하지 않은 비밀번호 입니다.", passwordCheckResponse));
    }

    @PostMapping("/phone")
    @Operation(summary = "인증번호 요청", description = "입력한 전화번호로 인증 번호 발송 (만료 시간: 3M) 응답")
    public ResponseEntity<ApiResponse<Long>> requestSmsCode(@RequestParam(value = "phone") String phone) {
        Long expired = registerService.requestCode(phone);
        return ResponseEntity.ok(ApiResponse.success("인증번호 발송", expired));
    }

    @PostMapping("/verify-code")
    @Operation(summary = "인증 번호 검증", description = "입력한 인증번호 요청 후 검증, 인증번호 검증이 완료된 전화번호 응답")
    public ResponseEntity<ApiResponse<String>> validSmsCode(@RequestParam(value = "phone") String phone,
                                                            @RequestParam(value = "code") String code) {
        registerService.validCode(phone, code);
        return ResponseEntity.ok(ApiResponse.success("인증번호 검증 성공", phone));
    }
}