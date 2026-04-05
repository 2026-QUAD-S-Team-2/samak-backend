package com.oaosis.samak.domain.report.dto.response;

import com.oaosis.samak.domain.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReportCreateResponse(
        @Schema(description = "신고 ID", example = "1")
        Long id
) {
    public static ReportCreateResponse from(Report report) {
        return new ReportCreateResponse(report.getId());
    }
}
