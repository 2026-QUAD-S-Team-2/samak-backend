package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.global.entity.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "분석 아이템 상세 응답")
public record AnalysisItemDetailResponse(
        @Schema(description = "분석 아이템 ID", example = "1")
        Long id,
        @Schema(description = "출처 URL", example = "https://www.example.com/job-posting")
        String sourceUrl,
        @Schema(description = "국가 코드", example = "KR")
        String countryCode,
        @Schema(description = "지역 ID", example = "1")
        Long regionId,

        @Schema(description = "연락 타입", example = "TELEGRAM")
        ContactType contactType,

        @Schema(description = "회사명", example = "OAOSIS Inc.")
        String companyName,

        @Schema(description = "제안 연봉", example = "3000000")
        BigDecimal salary,

        @Schema(description = "분석 상태", example = "COMPLETED")
        AnalysisStatus status,

        @Schema(description = "생성 날짜")
        LocalDateTime createdAt
) {
    public static AnalysisItemDetailResponse of(AnalysisItem item) {
        return new AnalysisItemDetailResponse(
                item.getId(),
                item.getSourceUrl(),
                item.getCountry().getCode(),
                item.getCity() != null ? item.getCity().getId() : null,
                item.getContactType(),
                item.getCompanyName(),
                item.getSalary() != null ? item.getSalary() : null,
                item.getStatus(),
                item.getCreatedAt()
        );
    }
}