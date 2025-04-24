package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.users.UsersRegisterDTO;
import kr.co.pei.pei_app.application.service.users.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.pei.pei_app.application.dto.api.ApiResult.*;

@Tag(name = "REGISTER_API", description = "사용자 계정 인증 및 등록을 위한 API")
@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @Operation(summary = "회원가입 요청", description = "모든 인증 완료 후 회원 가입 요청을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 가입 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원가입 성공 예시",
                                    summary = "회원가입 성공 응답 예제",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "가입이 완료 되었습니다.",
                                              "timestamp": "2025-03-26T03:16:29.459Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 유효성 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원가입 유효성 검증 실패 예시",
                                    summary = "회원 가입 검증 실패 응답 예제",
                                    value = """
                                            {
                                            "status" : 400,
                                            "message" : "올바른 형식이 아니거나 계정 인증을 완료하지 않은 않았습니다.",
                                            "timestamp": "2025-03-26T03:16:29.459Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원 가입 실패 예시 (서버오류)",
                                    summary = "서버 오류 가입 실패 예제",
                                    value = """
                                            {
                                            "status" : 500,
                                            "message" : "서버 오류로 인해 가입에 실패하였습니다. 다시 시도하거나 관리자에게 문의 해주세요",
                                            "timestamp" : "2025-03-26T03:16:29.459Z"
                                            }
                                            """
                            )
                    )
            ),
    })
    @PostMapping
    public ResponseEntity<ApiResult<String>> register(@Valid @RequestBody UsersRegisterDTO registerDTO) {
        registerService.register(registerDTO);
        return ResponseEntity.ok(success("가입이 완료 되었습니다."));
    }

    // TODO 계정 규칙을 어떻게 결정할지 정해지지 않아서 중복 계정 확인만 구현
    @Operation(summary = "계정 검증", description = "회원 가입 시 입력한 계정이 사용 가능한 계정인지 확인 (1.중복 계정 + 2.계정 규칙)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용 가능 계정",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                             name = "계정 검증 성공",
                             summary = "계정 검증 성공 응답 예제",
                             value = """
                                     { 
                                     "status" : 200,
                                     "message" : "사용 가능한 계정입니다",
                                     "timestamp" : "2025-03-26T03:16:29.459Z"
                                     }
                                     """
                            )

                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                name = "잘못된 요청 예시",
                                summary = "잘못된 요청 응답 예제",
                                value = """
                                        {
                                        "status" : 400,
                                        "message" : "사용할 수 없는 형식이거나, 잘못된 요청입니다",
                                        "timestamp": "2025-03-26T03:16:29.459Z"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "계정 중복 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "중복 검증 실패 예시",
                                    summary = "중복 검증 실패 예제",
                                    value = """
                                            {
                                                "status" : 409,
                                                "message" : "이미 사용중인 계정입니다",
                                                "timestamp" : "2025-03-26T03:16:29.459Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/check-username")
    public ResponseEntity<ApiResult<String>> existUsername(@Parameter(description = "사용할 계정 입력") @RequestParam("username") String username) {
        if (username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                    .body(error(HttpStatus.BAD_REQUEST.value(), "올바른 형식의 계정이 아닙니다.", null));
        }

        boolean existByUsername = registerService.existByUsername(username);

        if (existByUsername) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(error(HttpStatus.CONFLICT.value(), "이미 사용 중인 계정입니다.", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(success("사용 가능한 계정입니다.", username));
    }

    @Operation(summary = "인증번호 요청", description = "입력한 전화번호로 인증 번호 발송 (만료 시간: 3M) 응답")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증번호 발송 시 응답으로 레디스 저장된 expired 시간 반환",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증번호 성공 예시",
                                    summary = "인증번호 발송 성공 요청 예제",
                                    value = """
                                            {
                                                "status" : 200,
                                                "message" : "인증번호 발송 완료",
                                                "timestamp" : "2025-03-26T04:53:30.501Z",
                                                "data" : 180
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse( // TODO VUE 에서 Watch 로 실시간 이벤트 감지할 지 서버 데이터에서 에러 응답 반환할지 고민 (앞단에서 간단하게 하는게 나을듯)
                    responseCode = "400",
                    description = "인증번호 발송 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증번호 발송 실패 예시",
                                    summary = "인증번호 발송 실패 응답 예제",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "인증번호 발송 실패",
                                                "timestamp" : "2025-03-26T04:53:30.501Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "OTP 저장 실패 (서버 내부 오류) DB 로직 실패 시 인증번호 발송 안됨",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증번호 REDIS 저장 실패 예시",
                                    summary = "REDIS 에 인증번호가 저장되지 않은 경우 응답 예제",
                                    value = """
                                            {
                                                "status" : 500,
                                                "message" : "Redis OTP 저장 실패: expiration 유효하지 않음",
                                                "timestamp" : "2025-03-26T04:53:30.501Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/phone")
    public ResponseEntity<ApiResult<Long>> requestSmsCode(@Parameter(description = "사용자 전화번호") @RequestParam(value = "phone") String phone) {
        Long expired = registerService.requestCode(phone);
        return ResponseEntity.ok(success("인증번호 발송 완료", expired));
    }

    @Operation(summary = "인증번호 검증", description = "입력한 인증번호 요청 후 검증, 인증번호 검증이 완료된 휴대폰번호 응답")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증번호 검증 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 번호 검증 성공 예시",
                                    summary = "인증 번호 검증 성공 응답 예제",
                                    value = """
                                            {
                                                "status" : 200,
                                                "message" : "인증번호 검증 성공",
                                                "timestamp": "2025-03-26T04:53:30.501Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "인증번호 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = {@ExampleObject(
                                    name = "인증 번호 검증 실패 예시 1",
                                    summary = "인증 번호가 존재하지 않음",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "인증번호가 존재하지 않습니다.",
                                                "timestamp": "2025-03-26T04:53:30.501Z"
                                            
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "인증 번호 검증 실패 예시 2",
                                    summary = "잘못된 인증번호 입력",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "잘못된 인증번호 입니다.",
                                                "timestamp": "2025-03-26T04:53:30.501Z"
                                            }
                                            """
                            ),
                                    @ExampleObject(
                                            name = "인증번호 검증 실패 예시 3",
                                            summary = "인증번호 만료",
                                            value = """
                                                    {
                                                        "status" : 400,
                                                        "message" : "인증번호 만료 시간이 지났습니다.",
                                                        "timestamp": "2025-03-26T04:53:30.501Z"
                                                    }
                                                    """
                                    )
                            }
                            )
            )
    })
    @PostMapping("/verify-code")
    public ResponseEntity<ApiResult<String>> validSmsCode(@Parameter(description = "사용자 전화번호") @RequestParam(value = "phone") String phone,
                                                          @Parameter(description = "응답된 인증코드") @RequestParam(value = "code") String code) {
        registerService.validCode(phone, code);
        return ResponseEntity.ok(success("인증번호 검증 성공", phone));
    }
}