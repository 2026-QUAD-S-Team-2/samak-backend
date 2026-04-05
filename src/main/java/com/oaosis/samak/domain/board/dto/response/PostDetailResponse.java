package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "카테고리 (EXPERIENCE: 피해 경험담, FRAUD_VOTE: 사기 의심 투표)", example = "EXPERIENCE")
        PostCategory category,
        @Schema(description = "제목", example = "이 회사 사기인가요?")
        String title,
        @Schema(description = "내용", example = "입사 후 급여가 지급되지 않았습니다.")
        String content,
        @Schema(description = "작성자 닉네임", example = "홍길동")
        String authorNickname,
        @Schema(description = "좋아요 수", example = "10")
        Long likeCount,
        @Schema(description = "댓글 수", example = "5")
        Long commentCount,
        @Schema(description = "이미지 URL 목록")
        List<String> imageUrls,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {

    public static PostDetailResponse of(Post post, long likeCount, long commentCount, List<String> imageUrls) {
        return new PostDetailResponse(
                post.getId(),
                post.getCategory(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                likeCount,
                commentCount,
                imageUrls,
                post.getCreatedAt()
        );
    }
}
