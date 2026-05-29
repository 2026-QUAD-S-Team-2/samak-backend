package com.oaosis.samak.domain.report.dto.response;

import com.oaosis.samak.global.entity.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record ReportDetailResponse(
        @Schema(description = "회사명", example = "ABC Corporation")
        String companyName,

        @Schema(description = "신고 건수", example = "2")
        Long reportCount,

        @Schema(description = "최근 신고 날짜")
        LocalDateTime latestReportedAt,

        @Schema(description = "연락 수단 목록")
        List<ContactMethodResponse> contactMethods,

        @Schema(description = "피해 내용 목록")
        List<DamageResponse> damages,

        @Schema(description = "증거 이미지 URL 목록")
        List<String> evidenceImageUrls
) {
    public record ContactMethodResponse(
            @Schema(description = "연락 수단 유형", example = "TELEGRAM")
            ContactType type,

            @Schema(description = "연락처 값 목록", example = "[\"@abc_corp\"]")
            List<String> values
    ) {
    }

    public record DamageResponse(
            @Schema(description = "신고 ID", example = "1")
            Long reportId,

            @Schema(description = "신고자 마스킹 이름", example = "sy********님")
            String reporterName,

            @Schema(description = "피해 내용", example = "돈부터 먼저 입금하라고 하는 회사가 어디있습니까?")
            String reason,

            @Schema(description = "신고 날짜")
            LocalDateTime reportedAt
    ) {
    }
}
