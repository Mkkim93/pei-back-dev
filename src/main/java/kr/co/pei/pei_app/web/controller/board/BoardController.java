package kr.co.pei.pei_app.web.controller.board;

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
import kr.co.pei.pei_app.application.dto.board.BoardCreateDTO;
import kr.co.pei.pei_app.application.dto.board.BoardDetailDTO;
import kr.co.pei.pei_app.application.dto.board.BoardFindDTO;
import kr.co.pei.pei_app.application.dto.board.BoardUpdateDTO;
import kr.co.pei.pei_app.application.service.board.BoardService;
import kr.co.pei.pei_app.config.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.*;

@Tag(name = "BOARD_API", description = "게시글 관리를 위한 API")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "글 목록 조회", description = "게시글 페이징 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "게시글 리스트",
            content = @Content(schema = @Schema(implementation = ApiResult.class),
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "게시글 페이징 조회 예시",
                            summary = "작성 날짜 기준 오름차 순 / 최신글 10개 조회",
                            value = """
                                     {
                                       "status": 200,
                                       "message": "게시글 리스트 조회 성공",
                                       "timestamp": "2025-04-04T14:25:02.124643",
                                       "data": {
                                         "content": [
                                           {
                                             "id": 77,
                                             "title": "알림 전송 제목",
                                             "content": "알림을 보내기 위한 게시글 입니다.",
                                             "createAt": "2025-03-31T11:33:22.655005",
                                             "updateAt": "2025-03-31T11:33:22.655005",
                                             "writer": "관리자2",
                                             "views": 40,
                                             "roleType": "사용자",
                                             "usersId" : 6
                                           },
                                           {
                                             "id": 76,
                                             "title": "알림 전송 제목",
                                             "content": "알림을 보내기 위한 게시글 입니다.",
                                             "createAt": "2025-03-31T11:26:41.89088",
                                             "updateAt": "2025-03-31T11:26:41.89088",
                                             "writer": "관리자2",
                                             "views": 32,
                                             "roleType": "사용자",
                                             "usersId" : 6
                                           },
                                           {
                                             "id": 75,
                                             "title": "알림 전송 제목",
                                             "content": "알림을 보내기 위한 게시글 입니다.",
                                             "createAt": "2025-03-31T11:16:49.484734",
                                             "updateAt": "2025-03-31T11:16:49.484734",
                                             "writer": "관리자2",
                                             "views": 12,
                                             "roleType": "사용자",
                                             "usersId" : 6
                                           },
                                           {
                                             "id": 74,
                                             "title": "알림 전송 제목",
                                             "content": "알림을 보내기 위한 게시글 입니다.",
                                             "createAt": "2025-03-31T10:52:21.920275",
                                             "updateAt": "2025-03-31T10:52:21.920275",
                                             "writer": "관리자2",
                                             "views": 16,
                                             "roleType": "사용자",
                                             "usersId" : 6
                                           },
                                           {
                                             "id": 73,
                                             "title": "알림 전송 제목",
                                             "content": "알림을 보내기 위한 게시글 입니다.",
                                             "createAt": "2025-03-31T10:50:26.862231",
                                             "updateAt": "2025-03-31T10:50:26.862231",
                                             "writer": "관리자2",
                                             "views": 23,
                                             "roleType": "사용자",
                                             "usersId" : 6
                                           }
                                         ],
                                         "page": {
                                           "size": 5,
                                           "number": 0,
                                           "totalElements": 61,
                                           "totalPages": 13
                                         }
                                       }
                                     }
                                    """
                    )
            )
    )
    @GetMapping
    public ResponseEntity<ApiResult<Page<BoardFindDTO>>> findPage(
            @ParameterObject @PageableDefault(sort = "createdAt", direction = DESC, page = 0, size = 5) Pageable pageable,
            @Parameter(description = "검색 (제목 or 내용)") @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        Page<BoardFindDTO> boardList = boardService.pages(pageable, searchKeyword);
        return ResponseEntity.ok(ApiResult.success("게시글 리스트 조회 성공", boardList));
    }

    @Operation(summary = "상세 조회", description = "게시글 상세 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 상세 조회",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 상세 조회 예시",
                                    summary = "게시글 상세 조회 응답 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "게시글 상세 페이지",
                                                "timestamp": "2025-03-26T17:03:29.902639",
                                                "data": {
                                                    "id": 34,
                                                    "title": "포스트맨에서 201 테스트",
                                                    "content": "내용입니다",
                                                    "writer": "관리자1",
                                                    "updatedAt": "2025-03-26T16:59:24.404693",
                                                    "views": 0
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "게시글이 존재하지 않거나 로그인이 시간이 만료되어 접근이 불가 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 조회 실패 예시",
                                    summary = "게시글이 삭제 되었을 경우 응답 예시",
                                    value = """
                                            {
                                                "status": 400,
                                                "message": "게시글이 존재하지 않습니다.",
                                                "timeStamp": "2025-03-26T17:04:30.243475"
                                            }
                                            """
                            )

                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "게시글 조회 시 오류 (서버 내부 오류)",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 조회 오류 (서버 내부 오류)",
                                    summary = "게시글 조회 시 서버 오류 응답 예시",
                                    value = """
                                            {
                                                "status" : 500,
                                                "message" : "죄송합니다. 게시글 조회 도중 서버 오류가 발생하였습니다. 다시 시도하시거나 관리자에게 문의해주세요.",
                                                "timeStamp": "2025-03-26T17:04:30.243475"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<BoardDetailDTO>> findDetail(
            @Parameter(description = "게시글 번호") @PathVariable("id") Long id) {
        BoardDetailDTO boardDetail = boardService.detail(id);
        return ResponseEntity.ok(ApiResult.success("게시글 상세 페이지", boardDetail));
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성 시 제목은 필수 입력 사항입니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 작성 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 작성 성공 예시",
                                    summary = "게시글 작성 성공 시 해당 게시글 PK 응답 예제",
                                    value = """
                                            {
                                                "status" : 201,
                                                "message" : "게시글이 성공적으로 작성 되었습니다.",
                                                "timestamp": "2025-03-26T16:20:44.987059",
                                                "data" : "1"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse( // TODO 파일 엔티티 연동 시 사용
                    responseCode = "413",
                    description = "게시글 작성 실패",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "multipart/form-data",
                            examples = @ExampleObject(
                                    name = "유효성 검사 실패",
                                    summary = "파일 크기 등 제한 오류",
                                    value = """
                                                "status" : 413,
                                                "message" : "파일 크기가 너무 큽니다. 파일 크기는 2GB 이하만 업로드 가능합니다.",
                                                "timestamp" : "2025-03-26T16:20:44.987059"
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "게시글 작성 실패 (서버 내부 오류)",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 작성 실패",
                                    summary = "서버 내부 DB(트랜잭션 또는 내부 로직 오류)",
                                    value = """
                                                {
                                                    "status" : 500,
                                                    "message" : "죄송합니다. 서버 내부 오류로 인해 게시글 작성에 실패 하였습니다. 다시 시도하거나 관리자에게 문의 해주세요.",
                                                    "timestamp": "2025-03-26T16:20:44.987059"
                                                }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResult<Long>> created(@Valid @RequestBody BoardCreateDTO boardCreateDTO) {
        Long boardId = boardService.create(boardCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(HttpStatus.CREATED.value(), "게시글이 성공적으로 작성 되었습니다.", boardId));
    }

    // TODO 게시글 수정 목록 및 파일 수정 로직 추가, 게시글 수정 실패 시 응답 메세지 미정
    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PatchMapping
    public ResponseEntity<ApiResult<Boolean>> updated(@RequestBody BoardUpdateDTO boardUpdateDTO) {
        Boolean updated = boardService.update(boardUpdateDTO);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResult.error(HttpStatus.BAD_REQUEST.value(),
                            "UPDATED_FAILED",
                    "업데이트에 실패하였습니다.", false));
        }
        return ResponseEntity.ok(ApiResult.success("업데이트에 성공하였습니다.", true));
    }

    // TODO 파일 삭제 추가 예정
    @Operation(summary = "게시글 삭제", description = "글 번호 기준으로 게시글 삭제. 게시글 삭제 시, 해당 게시글에 포함돤 파일 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글이 삭제 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 삭제 성공 예시",
                                    summary = "게시글 삭제 성공 응답 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "게시글이 정상적으로 삭제 되었습니다.",
                                                "timestamp": "2025-03-26T17:13:12.741289"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "삭제하려는 게시글이 존재 하지 않는 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "삭제 게시글 없을 떄 예시",
                                    summary = "삭제 게시글 없을 때 응답 예제",
                                    value = """
                                            {
                                                "status" : 404,
                                                "message" : "삭제 대상 게시글이 존재 하지 않습니다.",
                                                "timestamp": "2025-03-26T17:22:18.453368"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류로 인해 게시글 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 삭제 실패 (서버 내부 오류)",
                                    summary = "서버 내부 DB(트랜잭션, 내부 로직 등) 오류 응답 예시",
                                    value = """
                                                {
                                                    "status" : 500,
                                                    "message" : "게시글 삭제 중 오류가 발생했습니다.",
                                                    "timestamp": "2025-03-26T17:22:18.453368"
                                                }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping
    public ResponseEntity<ApiResult<String>> deleted(@Parameter(description = "게시글 번호") @RequestParam("id") List<Long> boardIds) {
        boardService.delete(boardIds);
        return ResponseEntity.ok(ApiResult.success("게시글이 정상적으로 삭제 되었습니다."));
    }
}
