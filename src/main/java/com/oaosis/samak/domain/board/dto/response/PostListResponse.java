package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostListResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "카테고리 (EXPERIENCE: 피해 경험담, FRAUD_VOTE: 사기 의심 투표)", example = "EXPERIENCE")
        PostCategory category,
        @Schema(description = "제목", example = "이 회사 사기인가요?")
        String title,
        @Schema(description = "좋아요 수", example = "10")
        Long likeCount,
        @Schema(description = "댓글 수", example = "5")
        Long commentCount,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {
}
