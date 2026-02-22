package com.oaosis.samak.infra.openFeign.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;

import java.util.List;

public record AIAnalyzeResponse(
        @JsonProperty("analysisId")
        String analysisId,

        @JsonProperty("fraudProbability")
        Double fraudProbability,

        @JsonProperty("riskScore")
        Integer riskScore,

        @JsonProperty("riskLevel")
        String riskLevel,

        @JsonProperty("riskSignals")
        List<String> riskSignals,

        @JsonProperty("travelBanRegionsMatched")
        List<String> travelBanRegionsMatched,

        @JsonProperty("message")
        String message
) {

        public static AIAnalysisResult toEntity(AnalysisItem analysisItem, AIAnalyzeResponse response) {
                return new AIAnalysisResult(
                        analysisItem,
                        response.riskScore(),
                        response.riskLevel(),
                        response.message()
                );
        }
}