package com.oaosis.samak.domain.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScrapResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId,
        @Schema(description = "좋아요 수", example = "42")
        Long likeCount
) {

    public static ScrapResponse of(Long postId, long likeCount) {
        return new ScrapResponse(postId, likeCount);
    }
}
