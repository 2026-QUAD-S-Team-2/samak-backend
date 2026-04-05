package com.oaosis.samak.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @Schema(description = "댓글 내용", example = "저도 비슷한 경험이 있습니다.")
        @NotBlank
        String content
) {
}
