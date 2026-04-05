package com.oaosis.samak.domain.board.presentation;

import com.oaosis.samak.domain.board.application.BoardService;
import com.oaosis.samak.domain.board.dto.request.FraudVoteRequest;
import com.oaosis.samak.domain.board.dto.request.PostCreateRequest;
import com.oaosis.samak.domain.board.dto.response.FraudVoteResponse;
import com.oaosis.samak.domain.board.dto.response.PostCreateResponse;
import com.oaosis.samak.domain.board.dto.response.PostDetailResponse;
import com.oaosis.samak.domain.board.dto.response.PostListResponse;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.response.CursorResponse;
import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판", description = "게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시글 등록", description = "피해 경험담 또는 사기 의심 투표 게시글을 등록합니다.")
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostCreateResponse>> createPost(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostCreateResponse response = boardService.createPost(user.getEmail(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 커서 기반 페이지네이션으로 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<CursorResponse<PostListResponse>>> getPostList(
            @Parameter(description = "카테고리 필터 (미입력 시 전체 조회, EXPERIENCE: 피해 경험담, FRAUD_VOTE: 사기 의심 투표)")
            @RequestParam(required = false) PostCategory category,
            @Parameter(description = "커서 (이전 응답의 nextCursor 값, 첫 페이지는 미입력)")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "페이지 크기 (기본값: 10)")
            @RequestParam(defaultValue = "10") int size
    ) {
        CursorResponse<PostListResponse> response = boardService.getPostList(category, cursor, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetail(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long postId
    ) {
        PostDetailResponse response = boardService.getPostDetail(postId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "사기 의심 투표", description = "사기 의심 카테고리 게시글에 투표합니다. 1인 1회만 가능합니다.")
    @PostMapping("/posts/{postId}/fraud-vote")
    public ResponseEntity<ApiResponse<Void>> castFraudVote(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody FraudVoteRequest request
    ) {
        boardService.castFraudVote(postId, user.getEmail(), request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "사기 의심 투표 결과 조회")
    @GetMapping("/posts/{postId}/fraud-vote")
    public ResponseEntity<ApiResponse<FraudVoteResponse>> getFraudVoteResult(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long postId
    ) {
        FraudVoteResponse response = boardService.getFraudVoteResult(postId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
