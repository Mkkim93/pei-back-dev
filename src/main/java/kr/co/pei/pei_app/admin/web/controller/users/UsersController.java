package kr.co.pei.pei_app.admin.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import kr.co.pei.pei_app.admin.application.dto.api.ApiResult;
import kr.co.pei.pei_app.admin.application.dto.users.PasswordRequest;
import kr.co.pei.pei_app.admin.application.dto.users.UsersDetailDTO;
import kr.co.pei.pei_app.admin.application.dto.users.UsersUpdateDTO;
import kr.co.pei.pei_app.admin.application.service.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kr.co.pei.pei_app.admin.application.dto.api.ApiResult.error;
import static kr.co.pei.pei_app.admin.application.dto.api.ApiResult.success;


@Tag(name = "USERS_API", description = "로그인 후 사용자의 정보를 관리하기 위한 API")
@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @Operation(summary = "내 정보 조회", description = "내 정보 상세 조회를 위한 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "내 정보 조회",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "내 정보 조회 예시",
                                    summary = "내 정보 조회 응답 예제",
                                    value = """
                                            {
                                            "status": 200,
                                              "message": "사용자 정보",
                                              "timestamp": "2025-03-26T15:05:06.617087",
                                              "data": {
                                                "username": "user1",
                                                "name": "관리자1",
                                                "phone": "01055072536",
                                                "mail": "king00314@naver.com"
                                              }
                                            }
                                            """
                            )
                    )
            ),
    })
    @GetMapping("/profile")
    public ResponseEntity<ApiResult<UsersDetailDTO>> detail() {
        UsersDetailDTO userInfo = usersService.detail();
        return ResponseEntity.ok(success("내 정보", userInfo));
    }

    @Operation(summary = "계정 찾기", description = "사용자 전화번호로 인증 번호 발송")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 번호가 발송 되었습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 번호 발송 예시",
                                    summary = "인증 번호 발송 응답 예제",
                                    value = """
                                            {
                                                "status" : 200,
                                                "message" : "인증번호가 발송 되었습니다.",
                                                "timestamp": "2025-03-26T15:05:06.617087",
                                                "data": "01055072536"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "인증 번호 저장에 실패 했습니다.",
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
    @PostMapping("/recover-username")
    public ResponseEntity<ApiResult<String>> recoverUsername(
            @Parameter(description = "사용자 전화번호", example = "00012345678") @RequestParam("phone") String phone) {
        usersService.recoverUsername(phone);
        return ResponseEntity.ok(success("인증번호가 발송 되었습니다.", phone));
    }

    @Operation(summary = "인증번호 검증 -> 계정명 응답", description = "사용자 인증번호 검증 성공 시 사용자 계정 응답")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "인증 성공 예시",
                                summary = "인증 성공 응답 예시",
                                value = """
                                        {
                                            "status" : 200,
                                            "message" : "인증 성공",
                                            "timestamp": "2025-03-26T15:05:06.617087",
                                            "data" : "user1"
                                        }
                                        """
                        )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "인증에 성공, 계정 존재 하지 않음",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 성공, 계정이 존재하지 않을 떄 예시",
                                    summary = "인증 성공, 계정 존재하지 않을 때 응답 예제",
                                    value = """
                                            {
                                                "status" : 204,
                                                "errorCode" : "NOT_FIND_ID",
                                                "message" : "존재하지 않는 계정입니다",
                                                "timestamp" : "2025-03-26T15:05:06.617087"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 인증 실패(시간 만료, 코드 불일치 등)",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = {
                            @ExampleObject(
                                    name = "잘못된 요청 1",
                                    summary = "인증 실패, 응답 코드 만료",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "인증 번호 입력 시간이 만료 되었습니다. 인증번호를 다시 요청해주세요",
                                                "timestamp": "2025-03-26T15:05:06.617087"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "잘못된 요청 2",
                                    summary = "인증 코드가 존재 하지 않을 경우",
                                    value = """
                                        {
                                            "status" : 400,
                                            "message" : "인증번호가 존재 하지 않습니다.",
                                            "timestamp": "2025-03-26T15:05:06.617087"
                                        }
                                        """
                            ),
                            @ExampleObject(
                                    name = "잘못된 요청 3",
                                    summary = "입력한 인증번호가 발급받은 인증번호와 일치 하지 않는 경우",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "잘못된 인증번호 입니다 다시 입력해주세요.",
                                                "timestamp": "2025-03-26T15:05:06.617087"
                                            }
                                            """
                            )}
                    )
            )
    })
    @PostMapping("/receiver-username")
    public ResponseEntity<ApiResult<String>> responseUsername(
            @Parameter(description = "사용자 전화번호", example = "00012345678") @RequestParam("phone") String phone,
            @Parameter(description = "인증번호 6자리", example = "000000") @RequestParam("code")
            @NotBlank(message = "인증번호 6자리를 입력해주세요.") String code) {
        String username = usersService.requestUsername(phone, code);
        if (username == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(error(HttpStatus.NO_CONTENT.value(), "NOT_FIND_ID", "존재하지 않는 계정입니다"));
        }
        return ResponseEntity.ok(success("인증 성공", username));
    }

    @Operation(summary = "비밀번호 검증", description = "개인 정보 접근 시 사용자의 현재 비밀번호 검증")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 검증 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "비밀 번호 검증 성공 예시",
                                    summary = "비밀 번호 검증 성공 응답 예시",
                                    value = """
                                            {
                                                "status" : 200,
                                                "message" : "비밀번호 확인이 완료 되었습니다.",
                                                "timestamp": "2025-03-26T15:05:06.617087",
                                                "data" : true
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "비밀번호 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "비밀번호 검증 실패 예시",
                                    summary = "비밀번호 검증 실패 응답 예시",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "잘못된 비밀번호 입니다.",
                                                "timestamp": "2025-03-26T15:05:06.617087",
                                                "data" : false
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/auth-valid")
    public ResponseEntity<ApiResult<Boolean>> validPassword(@RequestBody PasswordRequest request) {
        boolean validPassword = usersService.myPasswordValid(request);
        if (!validPassword) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(error(HttpStatus.BAD_REQUEST.value(), "BAD_PASSWORD", "잘못된 비밀번호 입니다.", false));
        }
        return ResponseEntity.ok(success("비밀번호 확인이 완료 되었습니다.", true));
    }

    // TODO 수정할 데이터 결정 안됨
    @Operation(summary = "정보 수정", description = "별도의 인증 없이 사용자 정보 수정")
    @PatchMapping
    public ResponseEntity<String> updateUsers(@RequestBody UsersUpdateDTO usersUpdateDTO) {
        return null;
    }

    @Operation(summary = "비밀번호 찾기", description = "사용자 계정의 메일로 계정 비밀번호 변경 링크를 응답")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 링크 전송 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "비밀번호 링크 전송",
                                    summary = "사용자가 등록한 이메일로 비밀번호 재설정 링크 전송",
                                    value = """
                                            {
                                                "status" : 200,
                                                "message" : "비밀번호 변경 링크가 메일로 발송 되었습니다.",
                                                "timestamp": "2025-03-26T15:05:06.617087",
                                                "data" : "king00314@naver.com"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "존재하지 않는 계정",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정으로 계정 찾을 경우",
                                    summary = "계정이 존재 하지 안을 경우 응답 예시",
                                    value = """
                                            {
                                                "status" : 400,
                                                "message" : "사용자 정보가 존재 하지 않습니다.",
                                                "timestamp": "2025-03-26T15:05:06.617087"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "비밀번호 링크 전송 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "링크 전송 실패 (서버 내부 오류)",
                                    summary = "서버 내부 오류로 인한 링크 전송 실패 재시도 유도",
                                    value = """
                                            {
                                                "status" : 500,
                                                "message" : "메일 전송에 실패하였습니다.",
                                                "timestamp": "2025-03-26T15:05:06.617087"
                                               
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/auth-recover")
    public ResponseEntity<ApiResult<String>> recoverPassword(
            @Parameter(description = "사용자 계정", example = "userid@naver.com")
            @RequestParam("mail") String mail) {
        String userUUID = usersService.recoverPassword(mail);
        return ResponseEntity.ok(success("비밀번호 변경 링크가 메일로 발송 되었습니다.", userUUID));
    }

    // TODO 비밀번호 링크 클라이언트에서 전송 후 구현 완료 -> api Doc 작업
    @Operation(summary = "비밀번호 변경(재설정)", description = "임시 비밀번호 링크에서 비밀번호 재설정")
    @PostMapping("/auth-reset")
    public ResponseEntity<ApiResult<String>> modifyPassword(
            @Parameter(description = "변경할 비밀번호 (숫자,영문,특수문자 포함 8자리)", example = "aa1234@@@")
            @RequestParam("token") String token,
            @RequestParam("password") String password) {

        int count = usersService.resetPassword(token, password);

        if (count < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(400, "유효하지 않은 페이지입니다. 비밀번호 찾기를 처음부터 진행해 주세요."));
        }
        return ResponseEntity.ok(success("비밀번호가 성공적으로 변경 되었습니다."));
    }

    // TODO 영구 탈퇴 (향후 요구 사항에 따라 복구 할지 결정) 일단 식별자 만들어놓음
    @Operation(summary = "회원 탈퇴", description = "비밀번호 검증 완료 후 회원 탈퇴")
    @DeleteMapping("/{username}")
    public ResponseEntity<ApiResult<String>> delete(@Parameter(description = "계정명") @PathVariable("username") String username) {
        boolean deleted = usersService.deleteUsername(username);
        if (deleted) {
            return ResponseEntity.ok(success("계정 탈퇴가 완료 되었습니다."));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error(HttpStatus.BAD_REQUEST.value(),
                        "FAILED_WITH_DRAW",
                        "일시 적인 오류로 회원 탈퇴에 실패하였습니다."));
    }
}
