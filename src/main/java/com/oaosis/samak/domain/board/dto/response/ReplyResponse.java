package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReplyResponse(
        @Schema(description = "대댓글 ID", example = "5")
        Long id,
        @Schema(description = "내용", example = "저도 동일한 피해를 입었습니다.")
        String content,
        @Schema(description = "작성자 닉네임", example = "홍길동")
        String authorNickname,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {
    public static ReplyResponse from(Comment comment) {
        return new ReplyResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getNickname(),
                comment.getCreatedAt()
        );
    }
}
