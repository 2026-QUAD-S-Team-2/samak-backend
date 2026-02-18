package com.oaosis.samak.domain.analysis.dto.request;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.global.entity.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AnalysisItemCreateRequest(
        @Schema(description = "이미지 파일명 목록", example = "[\"image1.jpg\", \"image2.jpg\"]")
        List<String> imageNames,
        @Schema(description = "회사명", example = "OAOSIS Inc.")
        String companyName,
        @Schema(description = "국가 코드", example = "US")
        String countryCode,
        @Schema(description = "지역 ID", example = "1")
        Long cityId,
        @Schema(description = "연락 타입", example = "TELEGRAM")
        ContactType contactType,
        @Schema(description = "출처 URL")
        String sourceUrl,
        @Schema(description = "기타 사항", example = "이 회사는 빠르게 성장하고 있습니다.")
        String notes,
        @Schema(description = "제안 연봉", example = "3000000")
        Long salary
) {
}