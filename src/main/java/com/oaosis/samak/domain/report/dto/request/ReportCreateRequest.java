package com.oaosis.samak.domain.report.dto.request;

import com.oaosis.samak.global.entity.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReportCreateRequest(
        @Schema(description = "기업 ID (선택)", example = "1")
        Long companyId,

        @Schema(description = "기업 이름", example = "ABC Corporation")
        String companyName,

        @Schema(description = "신고 사유", example = "사기 의심 구인 공고입니다.")
        String reason,

        @Schema(description = "증거 이미지 파일명 목록 (선택)", example = "[\"abc123.jpg\"]")
        List<String> imageNames,

        @Schema(description = "연락 수단 유형 (선택)", example = "TELEGRAM")
        ContactType contactType,

        @Schema(description = "연락처 값 (선택)", example = "@reporter_id")
        String contactValue
) {
}
