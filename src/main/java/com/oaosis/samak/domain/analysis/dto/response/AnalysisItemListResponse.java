package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "분석 아이템 목록 응답")
public record AnalysisItemListResponse(
        @Schema(description = "분석 아이템 ID", example = "1")
        Long id,

        @Schema(description = "회사명", example = "ABC Company")
        String companyName,

        @Schema(description = "국가 코드", example = "KR")
        String countryCode,

        @Schema(description = "생성 날짜")
        LocalDateTime createdAt,

        @Schema(description = "위험 점수", example = "75.50")
        BigDecimal riskScore
) {
    public static AnalysisItemListResponse of(AnalysisItem item, BigDecimal riskScore) {
        return new AnalysisItemListResponse(
                item.getId(),
                item.getCompanyName(),
                item.getCountryCode(),
                item.getCreatedAt(),
                riskScore
        );
    }
}

