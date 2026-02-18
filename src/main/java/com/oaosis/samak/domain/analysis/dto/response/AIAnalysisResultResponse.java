package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import io.swagger.v3.oas.annotations.media.Schema;

public record AIAnalysisResultResponse(
        @Schema(description = "위험 점수", example = "85")
        int riskScore,
        @Schema(description = "위험 수준", example = "HIGH")
        String riskLevel,
        @Schema(description = "분석 메시지", example = "이 회사는 높은 위험을 가지고 있습니다.")
        String message
) {

    public static AIAnalysisResultResponse from(AIAnalysisResult result) {
        return new AIAnalysisResultResponse(result.getRiskScore(), result.getRiskLevel(), result.getMessage());
    }
}