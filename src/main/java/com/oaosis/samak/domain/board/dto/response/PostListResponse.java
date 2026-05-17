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
        @Schema(description = "스크랩 수", example = "10")
        Long scrapCount,
        @Schema(description = "댓글 수", example = "5")
        Long commentCount,
        @Schema(description = "현재 사용자의 스크랩 여부", example = "true")
        Boolean isScrapped,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {

    public PostListResponse(
            Long id,
            PostCategory category,
            String title,
            Long scrapCount,
            Long commentCount,
            LocalDateTime createdAt
    ) {
        this(id, category, title, scrapCount, commentCount, false, createdAt);
    }

    public PostListResponse withIsScrapped(boolean isScrapped) {
        return new PostListResponse(id, category, title, scrapCount, commentCount, isScrapped, createdAt);
    }
}
