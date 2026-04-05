package com.oaosis.samak.domain.report.dto.response;

import com.oaosis.samak.global.entity.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReportHistoryResponse(
        @Schema(description = "회사명", example = "ABC Corporation")
        String companyName,

        @Schema(description = "식별자 종류", example = "TELEGRAM")
        ContactType identifierType,

        @Schema(description = "식별자 값", example = "@abc_corp")
        String identifierValue,

        @Schema(description = "신고 날짜")
        LocalDateTime reportedAt
) {
}
