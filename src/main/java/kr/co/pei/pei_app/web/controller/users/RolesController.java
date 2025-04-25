package kr.co.pei.pei_app.web.controller.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.users.UsersFindDTO;
import kr.co.pei.pei_app.application.service.users.RolesService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.pei.pei_app.application.dto.api.ApiResult.success;

@Tag(name = "USERS_ROLE_API", description = "최종 관리자가 유저 권한 부여")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolesController {

    private final RolesService rolesService;

    @Operation(summary = "전체 사용자 목록 조회", description = "최상위 관리자부터 목록 페이징을 위한 API 입니다.")
    @ApiResponse(responseCode = "200", description = "모든 사용자 정보",
            content = @Content(schema = @Schema(implementation = ApiResult.class),
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "사용자 목록 조회 예시",
                            summary = "사용자 목록 조회 응답 예제",
                            value = """
                                    {
                                    "status" : 200,
                                    "message" : "모든 사용자 정보",
                                    "timestamp": "2025-03-26T14:58:10.11471",
                                    "data": {
                                        "content": [
                                          {
                                            "id": 14,
                                            "username": "user6",
                                            "name": "관리자6",
                                            "phone": "01055072536",
                                            "mail": "king00314@naver.com",
                                            "createAt": "2025-03-19T17:04:57.673438",
                                            "roleType": "관리자"
                                          },
                                          {
                                            "id": 6,
                                            "username": "user1",
                                            "name": "관리자1",
                                            "phone": "01055072536",
                                            "mail": "king00314@naver.com",
                                            "createAt": "2025-03-16T18:09:43.873399",
                                            "roleType": "사용자"
                                          },
                                          {
                                            "id": 7,
                                            "username": "user2",
                                            "name": "관리자2",
                                            "phone": "1234",
                                            "mail": "king00314@naver.com",
                                            "createAt": "2025-03-17T12:25:48.549035",
                                            "roleType": "사용자"
                                          },
                                          {
                                            "id": 8,
                                            "username": "user3",
                                            "name": "관리자3",
                                            "phone": "12345",
                                            "mail": "king00314@naver.com",
                                            "createAt": "2025-03-17T12:27:17.047671",
                                            "roleType": "사용자"
                                          }
                                        ],
                                        "page": {
                                          "size": 10,
                                          "number": 0,
                                          "totalElements": 4,
                                          "totalPages": 1
                                        }
                                      }
                                    }
                                    """
                    )
            )
    )
    @GetMapping
    public ResponseEntity<ApiResult<Page<UsersFindDTO>>> findAllUsers(@ParameterObject @PageableDefault(
            sort = "roleType", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UsersFindDTO> userList = rolesService.findAllUsers(pageable);
        return ResponseEntity.ok(success("모든 사용자 정보", userList));
    }
}
