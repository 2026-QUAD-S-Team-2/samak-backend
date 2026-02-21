package com.oaosis.samak.infra.openFeign.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}

