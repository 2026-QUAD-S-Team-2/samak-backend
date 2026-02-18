package com.oaosis.samak.domain.analysis.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "국가 경고 응답")
public record CountryWarningResponse(
        @Schema(description = "경고 메시지", example = "이 회사의 최저임금은 해당 국가의 최저임금보다 높습니다.")
        String warningMessage
) {
}