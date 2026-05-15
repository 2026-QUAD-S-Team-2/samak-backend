package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.LocationInfo;
import io.swagger.v3.oas.annotations.media.Schema;

public record AIAnalysisResultResponse(
        @Schema(description = "위험 점수", example = "85")
        int riskScore,
        @Schema(description = "위험 수준", example = "HIGH")
        String riskLevel,
        @Schema(description = "분석 메시지", example = "이 회사는 높은 위험을 가지고 있습니다.")
        String message,
        @Schema(description = "위치 정보")
        LocationResponse location
) {

    public record LocationResponse(
            @Schema(description = "위치 원문", example = "삼성전자 수원사업장")
            String rawText,
            @Schema(description = "위도", example = "37.2636")
            Double lat,
            @Schema(description = "경도", example = "127.0286")
            Double lng,
            @Schema(description = "행정구역", example = "경기도 수원시")
            String adminLevel,
            @Schema(description = "지도 줌 레벨", example = "14")
            Integer zoom,
            @Schema(description = "위치 상태", example = "company")
            String status,
            @Schema(description = "뷰포트 북동쪽 좌표")
            ViewportPoint viewportNe,
            @Schema(description = "뷰포트 남서쪽 좌표")
            ViewportPoint viewportSw
    ) {
        public record ViewportPoint(Double lat, Double lng) {}
    }

    public static AIAnalysisResultResponse from(AIAnalysisResult result) {
        LocationInfo loc = result.getLocation();
        LocationResponse locationResponse = null;
        if (loc != null && loc.getRawText() != null) {
            locationResponse = new LocationResponse(
                    loc.getRawText(),
                    loc.getLat(),
                    loc.getLng(),
                    loc.getAdminLevel(),
                    loc.getZoom(),
                    loc.getLocationStatus(),
                    new LocationResponse.ViewportPoint(loc.getViewportNeLat(), loc.getViewportNeLng()),
                    new LocationResponse.ViewportPoint(loc.getViewportSwLat(), loc.getViewportSwLng())
            );
        }
        return new AIAnalysisResultResponse(result.getRiskScore(), result.getRiskLevel(), result.getMessage(), locationResponse);
    }
}