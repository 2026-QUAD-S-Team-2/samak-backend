package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisItemType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "분석 아이템 상세 응답")
public record AnalysisItemDetailResponse(
        @Schema(description = "분석 아이템 ID", example = "1")
        Long id,

        @Schema(description = "분석 타입", example = "JOB_POST")
        AnalysisItemType type,

        @Schema(description = "출처 URL")
        String sourceUrl,

        @Schema(description = "원본 텍스트")
        String rawText,

        @Schema(description = "국가 코드", example = "KR")
        String countryCode,

        @Schema(description = "회사명", example = "ABC Company")
        String companyName,

        @Schema(description = "급여", example = "3000000.00")
        BigDecimal salary,

        @Schema(description = "생성 날짜")
        LocalDateTime createdAt,

        @Schema(description = "AI 분석 결과")
        AnalysisSummaryDto analysisSummary
) {
    public static AnalysisItemDetailResponse of(AnalysisItem item, AnalysisSummaryDto analysisSummary) {
        return new AnalysisItemDetailResponse(
                item.getId(),
                item.getType(),
                item.getSourceUrl(),
                item.getRawText(),
                item.getCountryCode(),
                item.getCompanyName(),
                item.getSalary(),
                item.getCreatedAt(),
                analysisSummary
        );
    }

    @Schema(description = "AI 분석 요약")
    public record AnalysisSummaryDto(
            @Schema(description = "신뢰도", example = "75.50")
            BigDecimal score,

            @Schema(description = "분석 메시지")
            String message
    ) {
    }
}