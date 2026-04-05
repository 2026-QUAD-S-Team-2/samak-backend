package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostCreateResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "게시글 카테고리", example = "EXPERIENCE")
        PostCategory category,
        @Schema(description = "제목", example = "이 회사 사기인가요?")
        String title,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {

    public static PostCreateResponse from(Post post) {
        return new PostCreateResponse(
                post.getId(),
                post.getCategory(),
                post.getTitle(),
                post.getCreatedAt()
        );
    }
}
