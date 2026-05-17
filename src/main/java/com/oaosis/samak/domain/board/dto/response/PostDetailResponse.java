package com.oaosis.samak.domain.board.dto.response;

import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "카테고리 (EXPERIENCE: 피해 경험담, FRAUD_VOTE: 사기 의심 투표, AI_ANALYSIS: AI 분석 결과 공유)", example = "EXPERIENCE")
        PostCategory category,
        @Schema(description = "제목", example = "이 회사 사기인가요?")
        String title,
        @Schema(description = "내용", example = "입사 후 급여가 지급되지 않았습니다.")
        String content,
        @Schema(description = "작성자 닉네임", example = "홍길동")
        String authorNickname,
        @Schema(description = "스크랩 수", example = "10")
        Long scrapCount,
        @Schema(description = "댓글 수", example = "5")
        Long commentCount,
        @Schema(description = "현재 사용자의 스크랩 여부", example = "true")
        Boolean isScrapped,
        @Schema(description = "이미지 URL 목록")
        List<String> imageUrls,
        @Schema(description = "연결된 분석 아이템 ID (AI_ANALYSIS 카테고리일 때만 값 존재)", example = "1")
        Long analysisItemId,
        @Schema(description = "작성일시")
        LocalDateTime createdAt
) {

    public static PostDetailResponse of(
            Post post,
            long scrapCount,
            long commentCount,
            boolean isScrapped,
            List<String> imageUrls
    ) {
        return new PostDetailResponse(
                post.getId(),
                post.getCategory(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                scrapCount,
                commentCount,
                isScrapped,
                imageUrls,
                post.getAnalysisItem() != null ? post.getAnalysisItem().getId() : null,
                post.getCreatedAt()
        );
    }
}
