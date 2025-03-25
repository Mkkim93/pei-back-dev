package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.api.PasswordCheckResponse;
import kr.co.pei.pei_app.application.dto.users.UsersResponseDTO;
import kr.co.pei.pei_app.application.dto.users.UsersRegisterDTO;
import kr.co.pei.pei_app.application.service.users.RegisterService;
import kr.co.pei.pei_app.config.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REGISTER_API", description = "사용자 계정 인증 및 등록을 위한 API")
@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @Operation(summary = "회원가입 요청", description = "모든 인증 완료 후 회원 가입 요청을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 완료",
                    content = @Content(schema = @Schema(implementation = UsersRegisterDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 계정",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResult<UsersResponseDTO>> register(@Valid @RequestBody UsersRegisterDTO registerDTO) {
        UsersResponseDTO registerUsers = registerService.register(registerDTO);
        return ResponseEntity.ok(ApiResult.success("가입이 완료 되었습니다.", registerUsers));
    }

    // TODO 계정 규칙을 어떻게 결정할지 정해지지 않아서 중복 계정 확인만 구현
    @Operation(summary = "계정 검증", description = "회원 가입 시 입력한 계정이 사용 가능한 계정인지 확인 (1.중복 계정 + 2.계정 규칙)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 계정",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "409", description = "사용 중인 계정",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/check-username")
    public ResponseEntity<ApiResult<String>> existUsername(@Parameter(description = "사용할 계정 입력") @RequestParam("username") String username) {
        boolean existByUsername = registerService.existByUsername(username);

        if (existByUsername) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResult.error(HttpStatus.CONFLICT.value(), "EXIST_USERNAME", "사용 중인 계정 입니다."));
        }
        return ResponseEntity.ok(ApiResult.success("사용 가능한 계정입니다.", username));
    }

    @Operation(summary = "비밀번호 강도체크", description = "사용자가 입력한 비밀번호가 충분히 안전한지 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안전한 비밀번호",
                    content = @Content(schema = @Schema(implementation = PasswordCheckResponse.class))),
            @ApiResponse(responseCode = "400", description = "안전하지 않은 비밀번호",
                    content = @Content(schema = @Schema(implementation = PasswordCheckResponse.class)))
    })
    @PostMapping("/password-strength")
    public ResponseEntity<ApiResult<PasswordCheckResponse>> checkPasswordStrength(@Parameter(description = "사용할 비밀번호 (숫자, 문자, 특수문자 포함 8자리 이상)", example = "123@@45aa") @RequestParam("password") String password) {

        PasswordCheckResponse passwordCheckResponse = registerService.checkPasswordStrength(password);

        if (passwordCheckResponse.getGrade() > 3) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResult.success("안전한 비밀번호 입니다.", passwordCheckResponse));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(HttpStatus.BAD_REQUEST.value(),
                "BAD_PASSWORD","안전하지 않은 비밀번호 입니다.", passwordCheckResponse));
    }

    @Operation(summary = "인증번호 요청", description = "입력한 전화번호로 인증 번호 발송 (만료 시간: 3M) 응답")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 발송 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "인증번호 발송 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PostMapping("/phone")
    public ResponseEntity<ApiResult<Long>> requestSmsCode(@Parameter(description = "사용자 전화번호") @RequestParam(value = "phone") String phone) {
        Long expired = registerService.requestCode(phone);
        return ResponseEntity.ok(ApiResult.success("인증번호 발송", expired));
    }

    @Operation(summary = "인증번호 검증", description = "입력한 인증번호 요청 후 검증, 인증번호 검증이 완료된 전화번호 응답")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 검증 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "인증번호 검증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PostMapping("/verify-code")
    public ResponseEntity<ApiResult<String>> validSmsCode(@Parameter(description = "사용자 전화번호") @RequestParam(value = "phone") String phone,
                                                          @Parameter(description = "응답된 인증코드") @RequestParam(value = "code") String code) {
        registerService.validCode(phone, code);
        return ResponseEntity.ok(ApiResult.success("인증번호 검증 성공", phone));
    }
}