package com.oaosis.samak.domain.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScrapResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId,
        @Schema(description = "스크랩 수", example = "42")
        Long scrapCount
) {

    public static ScrapResponse of(Long postId, long scrapCount) {
        return new ScrapResponse(postId, scrapCount);
    }
}
