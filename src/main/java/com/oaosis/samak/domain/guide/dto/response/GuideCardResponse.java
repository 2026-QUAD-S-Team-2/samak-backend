package com.oaosis.samak.domain.guide.dto.response;

import com.oaosis.samak.domain.guide.entity.GuideCard;
import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record GuideCardResponse(
        Long id,
        @Schema(description = "가이드 카테고리 (PREVENTION: 예방, RESPONSE: 대응)", example = "PREVENTION")
        GuideCategory category,
        @Schema(description = "가이드 카드 이미지 URL", example = "https://cdn.example.com/guides/card1.png")
        String imageUrl,
        @Schema(description = "국가 코드 (ISO 3166-1 alpha-2)", example = "KR")
        String countryCode) {

    public static GuideCardResponse of(GuideCard guideCard, String imageUrl) {
        return new GuideCardResponse(
                guideCard.getId(),
                guideCard.getCategory(),
                imageUrl,
                guideCard.getCountryCode()
        );
    }
}
