package com.oaosis.samak.domain.board.dto.request;

import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostCreateRequest(
        @Schema(description = "게시글 카테고리 (EXPERIENCE: 피해 경험담, FRAUD_VOTE: 사기 의심 투표)", example = "EXPERIENCE")
        @NotNull
        PostCategory category,

        @Schema(description = "제목 (최대 100자)", example = "이 회사 사기인가요?")
        @NotBlank
        @Size(max = 100)
        String title,

        @Schema(description = "내용", example = "입사 후 급여가 지급되지 않았습니다.")
        @NotBlank
        String content,

        @Schema(description = "이미지 파일명 목록 (선택)", example = "[\"abc123.jpg\", \"def456.jpg\"]")
        List<String> imageNames
) {
}
