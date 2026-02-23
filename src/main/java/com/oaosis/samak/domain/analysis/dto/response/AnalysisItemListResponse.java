package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public record AnalysisItemListResponse(
        @Schema(description = "분석 아이템 ID", example = "1")
        Long id,

        @Schema(description = "회사명", example = "ABC Company")
        String companyName,
        
        @Schema(description = "국가명", example = "대한민국")
        String countryName,

        @Schema(description = "지역명", example = "서울특별시")
        String cityName,

        @Schema(description = "분석 상태", example = "COMPLETED")
        AnalysisStatus status,

        @Schema(description = "생성 날짜")
        LocalDateTime createdAt,

        @Schema(description = "신뢰도", example = "75.50")
        Integer score
) {
    public static AnalysisItemListResponse of(AnalysisItem item, Integer score) {
        return new AnalysisItemListResponse(
                item.getId(),
                item.getCompanyName(),
                item.getCountry().getName(),
                item.getCity().getName(),
                item.getStatus(),
                item.getCreatedAt(),
                score
        );
    }
}