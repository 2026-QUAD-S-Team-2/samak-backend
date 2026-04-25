package com.oaosis.samak.domain.member.dto.response;

import com.oaosis.samak.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 조회 응답")
public record MemberResponse(
        @Schema(description = "멤버 ID", example = "1")
        Long id,
        @Schema(description = "이메일", example = "samak@gmail.com")
        String email,
        @Schema(description = "닉네임", example = "행복한낙타")
        String nickname,
        @Schema(description = "프로필 이미지 URL", example = "https://s3.amazonaws.com/bucket/profile.png")
        String profileImageUrl,
        @Schema(description = "온보딩 완료 여부", example = "true")
        boolean isOnboarded
) {
    public static MemberResponse from(Member member, String profileImageUrl) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                profileImageUrl,
                member.isOnboarded()
        );
    }
}