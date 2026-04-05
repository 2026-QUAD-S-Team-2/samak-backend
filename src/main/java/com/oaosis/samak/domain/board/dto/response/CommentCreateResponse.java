package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long id,
        @Schema(description = "내용", example = "저도 비슷한 경험이 있습니다.")
        String content,
        @Schema(description = "작성자 닉네임", example = "홍길동")
        String authorNickname,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {
    public static CommentCreateResponse from(Comment comment) {
        return new CommentCreateResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getNickname(),
                comment.getCreatedAt()
        );
    }
}
