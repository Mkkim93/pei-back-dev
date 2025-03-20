package kr.co.pei.pei_app.web.controller.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pei.pei_app.application.dto.api.ApiResponse;
import kr.co.pei.pei_app.application.dto.board.CreateBoardDTO;
import kr.co.pei.pei_app.application.dto.board.DetailBoardDTO;
import kr.co.pei.pei_app.application.dto.board.FindBoardDTO;
import kr.co.pei.pei_app.application.dto.board.UpdateBoardDTO;
import kr.co.pei.pei_app.application.service.board.BoardService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FindBoardDTO>>> findPage(@PageableDefault(sort = "createdAt", direction = ASC) Pageable pageable,
                                                                    @Parameter(description = "검색 (제목 or 내용)") @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        Page<FindBoardDTO> boardList = boardService.pages(pageable, searchKeyword);
        return ResponseEntity.ok(ApiResponse.success("게시글 리스트", boardList));
    }

    @Operation(summary = "상세 조회", description = "게시글 상세 조회 API")
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<ApiResponse<DetailBoardDTO>> findDetail(@Parameter(description = "게시글 번호") @PathVariable("boardId") Long boardId) {
        DetailBoardDTO boardDetail = boardService.detail(boardId);
        return ResponseEntity.ok(ApiResponse.success("게시글 상세 페이지", boardDetail));
    }

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> created(@Valid @RequestBody CreateBoardDTO createBoardDTO) {
        boardService.create(createBoardDTO);
        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 작성 되었습니다."));
    }

    // TODO
    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @PatchMapping
    public ResponseEntity<ApiResponse<Boolean>> updated(@RequestBody UpdateBoardDTO updateBoardDTO) {
        Boolean updated = boardService.update(updateBoardDTO);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(),
                            "UPDATED_FAILED",
                    "업데이트에 실패하였습니다.", false));
        }
        return ResponseEntity.ok(ApiResponse.success("업데이트에 성공하였습니다.", true));
    }

    @Operation(summary = "게시글 삭제", description = "글 번호 기준으로 게시글 삭제. 게시글 삭제 시, 해당 게시글에 포함돤 파일 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleted(@Parameter(description = "게시글 번호") @RequestParam("boardId") List<Long> boardIds) {
        boolean exist = boardService.delete(boardIds);
        if (!exist) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "NO CONTENT", "삭제할 게시글이 존재 하지 않습니다."));
        }
        return ResponseEntity.ok(ApiResponse.success("게시글이 정상적으로 삭제 되었습니다."));
    }
}
