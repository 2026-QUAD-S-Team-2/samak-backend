package com.oaosis.samak.domain.board.dto.request;

import com.oaosis.samak.domain.board.entity.enums.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FraudVoteRequest(
        @Schema(description = "투표 유형 (FRAUD: 맞다, NOT_FRAUD: 아니다)", example = "FRAUD")
        @NotNull
        VoteType voteType
) {
}
