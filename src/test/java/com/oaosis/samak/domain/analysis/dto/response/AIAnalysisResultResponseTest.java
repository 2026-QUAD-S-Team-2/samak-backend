package com.oaosis.samak.domain.analysis.dto.response;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.LocationInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AIAnalysisResultResponseTest {

    @Test
    void from_withFullLocation_mapsAllLocationFields() {
        LocationInfo locationInfo = new LocationInfo(
                "삼성전자 수원사업장", 37.2636, 127.0286, "경기도 수원시", 14, "company",
                37.2700, 127.0350, 37.2570, 127.0220
        );
        AIAnalysisResult result = mock(AIAnalysisResult.class);
        when(result.getRiskScore()).thenReturn(85);
        when(result.getRiskLevel()).thenReturn("HIGH");
        when(result.getMessage()).thenReturn("높은 위험");
        when(result.getLocation()).thenReturn(locationInfo);

        AIAnalysisResultResponse response = AIAnalysisResultResponse.from(result);

        assertThat(response.riskScore()).isEqualTo(85);
        assertThat(response.riskLevel()).isEqualTo("HIGH");
        assertThat(response.message()).isEqualTo("높은 위험");
        assertThat(response.location()).isNotNull();
        assertThat(response.location().rawText()).isEqualTo("삼성전자 수원사업장");
        assertThat(response.location().lat()).isEqualTo(37.2636);
        assertThat(response.location().lng()).isEqualTo(127.0286);
        assertThat(response.location().adminLevel()).isEqualTo("경기도 수원시");
        assertThat(response.location().zoom()).isEqualTo(14);
        assertThat(response.location().status()).isEqualTo("company");
        assertThat(response.location().viewportNe().lat()).isEqualTo(37.2700);
        assertThat(response.location().viewportNe().lng()).isEqualTo(127.0350);
        assertThat(response.location().viewportSw().lat()).isEqualTo(37.2570);
        assertThat(response.location().viewportSw().lng()).isEqualTo(127.0220);
    }

    @Test
    void from_withNullLocation_returnsNullLocationInResponse() {
        AIAnalysisResult result = mock(AIAnalysisResult.class);
        when(result.getRiskScore()).thenReturn(50);
        when(result.getRiskLevel()).thenReturn("MEDIUM");
        when(result.getMessage()).thenReturn("보통 위험");
        when(result.getLocation()).thenReturn(null);

        AIAnalysisResultResponse response = AIAnalysisResultResponse.from(result);

        assertThat(response.location()).isNull();
    }

    @Test
    void from_withLocationHavingNullRawText_returnsNullLocationInResponse() {
        // Hibernate가 모든 컬럼이 null일 때 빈 LocationInfo를 반환하는 경우를 대비
        LocationInfo emptyLocation = new LocationInfo(null, null, null, null, null, null, null, null, null, null);
        AIAnalysisResult result = mock(AIAnalysisResult.class);
        when(result.getRiskScore()).thenReturn(50);
        when(result.getRiskLevel()).thenReturn("MEDIUM");
        when(result.getMessage()).thenReturn("메시지");
        when(result.getLocation()).thenReturn(emptyLocation);

        AIAnalysisResultResponse response = AIAnalysisResultResponse.from(result);

        assertThat(response.location()).isNull();
    }
}
