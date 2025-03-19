package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import kr.co.pei.pei_app.application.dto.api.ApiResponse;
import kr.co.pei.pei_app.application.dto.users.FindUsersDTO;
import kr.co.pei.pei_app.application.dto.users.PasswordRequest;
import kr.co.pei.pei_app.application.dto.users.UsersUpdateDTO;
import kr.co.pei.pei_app.application.dto.users.UsersDetailDTO;
import kr.co.pei.pei_app.application.service.users.UsersService;
import kr.co.pei.pei_app.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "USERS_API")
@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    @Operation(summary = "전체 사용자 목록 조회", description = "최상위 관리자부터 목록 페이징을 위한 API 입니다.")
    public ResponseEntity<ApiResponse<Page<FindUsersDTO>>> findAllUsers(@PageableDefault(
            size = 10,
            sort = "roleType",
            direction = Sort.Direction.ASC
    ) Pageable pageable) {
        Page<FindUsersDTO> userList = usersService.findAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("모든 사용자 정보", userList));
    }

    @GetMapping("/profile")
    @Operation(summary = "내 정보 조회", description = "사용자 정보를 상세 조회 하기 위한 API")
    public ResponseEntity<ApiResponse<UsersDetailDTO>> detail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UsersDetailDTO userInfo = usersService.detail(userDetails);
        return ResponseEntity.ok(ApiResponse.success("사용자 정보", userInfo));
    }

    @PostMapping("/recover-username")
    @Operation(summary = "계정 찾기", description = "사용자 전화번호로 인증 번호 발송")
    public ResponseEntity<ApiResponse<String>> recoverUsername(@RequestParam("phone") String phone) {
        usersService.recoverUsername(phone);
        return ResponseEntity.ok(ApiResponse.success("인증 번호가 발송 되었습니다.", phone));
    }

    @PostMapping("/receiver-username")
    @Operation(summary = "사용자 인증번호 요청 -> 계정명 응답", description = "사용자 인증번호 검증 성공 시 사용자 계정 응답")
    public ResponseEntity<ApiResponse<String>> responseUsername(@RequestParam("phone") String phone,
                                                                @RequestParam("code") @NotBlank(message = "인증번호 6자리를 입력해주세요.") String code) {
        String username = usersService.requestUsername(phone, code);
        if (username == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.error(HttpStatus.NO_CONTENT.value(), "존재하지 않는 계정입니다.", "확인되지 않은 계정입니다."));
        }
        return ResponseEntity.ok(ApiResponse.success("사용자 계정", username));
    }

    @PostMapping("/auth-recover")
    @Operation(summary = "비밀번호 찾기", description = "사용자 계정의 메일로 계정 비밀번호 변경 링크를 응답")
    public ResponseEntity<ApiResponse<String>> recoverPassword(@RequestParam("username") String username) {
        usersService.recoverPassword(username);
        return ResponseEntity.ok(ApiResponse.success("비밀번호 변경 링크가 메일로 발송 되었습니다.", username));
    }

    @PostMapping("/auth-valid")
    @Operation(summary = "비밀번호 검증", description = "개인 정보 접근 시 사용자의 현재 비밀번호 검증")
    public ResponseEntity<ApiResponse<Boolean>> validPassword(@RequestBody PasswordRequest request) {
        boolean validPassword = usersService.myPasswordValid(request);
        if (!validPassword) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "BAD_PASSWORD", "잘못된 비밀 번호 입니다.", false));
        }
        return ResponseEntity.ok(ApiResponse.success("비밀번호 확인이 완료 되었습니다.", true));
    }

    // TODO 수정할 데이터 결정 안됨
    @PatchMapping
    @Operation(summary = "정보 수정", description = "별도의 인증 없이 사용자 정보 수정")
    public ResponseEntity<String> updateUsers(@RequestBody UsersUpdateDTO usersUpdateDTO) {
        return null;
    }

    // TODO 비밀번호 링크를 클라이언트에서 전송 후 구현
    @PostMapping("/auth-reset")
    @Operation(summary = "비밀번호 변경(재설정)", description = "임시 비밀번호 링크에서 비밀번호 재설정")
    public ResponseEntity<ApiResponse<String>> modifyPassword(@RequestParam("password") String password,
                                                              @RequestParam("username") String username) {
//        Map<String, Object> responseMap = usersService.updatePassword(username, password);
//        return ResponseEntity.ok(responseMap);
        return null;
    }

    // TODO 영구 탈퇴 (향후 요구 사항에 따라 복구 할지 결정)
    @DeleteMapping("/{username}")
    @Operation(summary = "회원 탈퇴", description = "비밀번호 검증 완료 후 회원 탈퇴")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable("username") String username) {
        Boolean deleted = usersService.deleteUsername(username);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("계정 탈퇴가 완료 되었습니다."));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(),
                        "FAILED_WITH_DRAW",
                        "일시 적인 오류로 회원 탈퇴에 실패하였습니다."));
    }

}
