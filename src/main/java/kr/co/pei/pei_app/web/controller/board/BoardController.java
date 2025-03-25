package kr.co.pei.pei_app.web.controller.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pei.pei_app.application.dto.api.ApiResult;
import kr.co.pei.pei_app.application.dto.board.CreateBoardDTO;
import kr.co.pei.pei_app.application.dto.board.DetailBoardDTO;
import kr.co.pei.pei_app.application.dto.board.FindBoardDTO;
import kr.co.pei.pei_app.application.dto.board.UpdateBoardDTO;
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
    @ApiResponse(responseCode = "200", description = "게시글 리스트",
            content = @Content(schema = @Schema(implementation = ApiResult.class)))
    @GetMapping
    public ResponseEntity<ApiResult<Page<FindBoardDTO>>> findPage(
            @ParameterObject @PageableDefault(sort = "createdAt", direction = ASC) Pageable pageable,
            @Parameter(description = "검색 (제목 or 내용)") @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        Page<FindBoardDTO> boardList = boardService.pages(pageable, searchKeyword);
        return ResponseEntity.ok(ApiResult.success("게시글 리스트", boardList));
    }

    @Operation(summary = "상세 조회", description = "게시글 상세 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "게시글이 존재하지 않거나 로그인이 시간이 만료되어 접근이 불가 합니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<ApiResult<DetailBoardDTO>> findDetail(
            @Parameter(description = "게시글 번호")
            @PathVariable("boardId") Long boardId
    ) {
        DetailBoardDTO boardDetail = boardService.detail(boardId);
        return ResponseEntity.ok(ApiResult.success("게시글 상세 페이지", boardDetail));
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 작성 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "게시글 작성 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResult<String>> created(@Valid @RequestBody CreateBoardDTO createBoardDTO) {
        boardService.create(createBoardDTO);
        return ResponseEntity.ok(ApiResult.success("게시글이 성공적으로 작성 되었습니다."));
    }

    // TODO 게시글 수정 목록 및 파일 수정 로직 추가, 게시글 수정 실패 시 응답 메세지 미정
    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    @PatchMapping
    public ResponseEntity<ApiResult<Boolean>> updated(@RequestBody UpdateBoardDTO updateBoardDTO) {
        Boolean updated = boardService.update(updateBoardDTO);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResult.error(HttpStatus.BAD_REQUEST.value(),
                            "UPDATED_FAILED",
                    "업데이트에 실패하였습니다.", false));
        }
        return ResponseEntity.ok(ApiResult.success("업데이트에 성공하였습니다.", true));
    }

    @Operation(summary = "게시글 삭제", description = "글 번호 기준으로 게시글 삭제. 게시글 삭제 시, 해당 게시글에 포함돤 파일 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 삭제 완료",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류로 인해 게시글 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @DeleteMapping
    public ResponseEntity<ApiResult<String>> deleted(@Parameter(description = "게시글 번호") @RequestParam("boardId") List<Long> boardIds) {
        boolean exist = boardService.delete(boardIds);
        if (!exist) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResult.error(HttpStatus.BAD_REQUEST.value(), "NO CONTENT", "삭제할 게시글이 존재 하지 않습니다."));
        }
        return ResponseEntity.ok(ApiResult.success("게시글이 정상적으로 삭제 되었습니다."));
    }
}
