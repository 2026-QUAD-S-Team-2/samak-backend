package com.oaosis.samak.domain.report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReportListResponse(
        @Schema(description = "회사명", example = "ABC Corporation")
        String companyName,

        @Schema(description = "최근 신고 날짜")
        LocalDateTime latestReportedAt,

        @Schema(description = "신고 건수", example = "5")
        Long reportCount
) {
}
