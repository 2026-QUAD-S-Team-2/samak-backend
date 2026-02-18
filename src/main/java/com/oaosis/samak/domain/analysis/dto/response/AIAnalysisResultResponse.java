package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;

public record AIAnalysisResultResponse(
    int riskScore,
    String riskLevel,
    String message
) {

    public static AIAnalysisResultResponse from(AIAnalysisResult result) {
        return new AIAnalysisResultResponse(result.getRiskScore(), result.getRiskLevel(), result.getMessage());
    }
}