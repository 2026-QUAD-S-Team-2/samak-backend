package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long id,
        @Schema(description = "내용", example = "저도 비슷한 경험이 있습니다.")
        String content,
        @Schema(description = "작성자 닉네임", example = "홍길동")
        String authorNickname,
        @Schema(description = "작성일시")
        LocalDateTime createdAt,
        @Schema(description = "대댓글 목록")
        List<ReplyResponse> replies
) {
    public static CommentResponse of(Comment comment, List<ReplyResponse> replies) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getNickname(),
                comment.getCreatedAt(),
                replies
        );
    }
}
