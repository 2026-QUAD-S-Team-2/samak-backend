package com.oaosis.samak.domain.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FraudVoteResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId,
        @Schema(description = "사기 맞다 투표 수", example = "15")
        Long fraudCount,
        @Schema(description = "사기 아니다 투표 수", example = "5")
        Long notFraudCount,
        @Schema(description = "현재 사용자의 투표 여부 (비로그인 시 false)", example = "true")
        boolean isVoted
) {

    public static FraudVoteResponse of(Long postId, long fraudCount, long notFraudCount, boolean isVoted) {
        return new FraudVoteResponse(postId, fraudCount, notFraudCount, isVoted);
    }
}
