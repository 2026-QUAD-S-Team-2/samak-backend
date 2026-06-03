package com.oaosis.samak.infra.pubsub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.domain.analysis.repository.AIAnalysisResultRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.domain.country.entity.CountryMetricsSnapshot;
import com.oaosis.samak.domain.country.entity.WageUnit;
import com.oaosis.samak.domain.country.repository.CountryMetricsSnapshotRepository;
import com.oaosis.samak.infra.pubsub.dto.response.AnalysisResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PubSubAsyncAnalysisServiceTest {

    @Mock
    private AnalysisItemRepository analysisItemRepository;

    @Mock
    private AIAnalysisResultRepository aiAnalysisResultRepository;

    @Mock
    private CountryMetricsSnapshotRepository countryMetricsSnapshotRepository;

    @Mock
    private Publisher analysisRequestPublisher;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TransactionTemplate transactionTemplate;

    @InjectMocks
    private PubSubAsyncAnalysisService service;

    @Test
    void processAnalysisResult_withLocation_savesLocationToEntity() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());

        AnalysisResponse.Location location = new AnalysisResponse.Location(
                "삼성전자 수원사업장", 37.2636, 127.0286, "경기도 수원시", 14, "company",
                new AnalysisResponse.Location.ViewportPoint(37.2700, 127.0350),
                new AnalysisResponse.Location.ViewportPoint(37.2570, 127.0220)
        );
        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.87, 85, "HIGH", List.of("signal1"), List.of(), "분석 메시지", location
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);

        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        AIAnalysisResult saved = captor.getValue();
        assertThat(saved.getLocation()).isNotNull();
        assertThat(saved.getLocation().getRawText()).isEqualTo("삼성전자 수원사업장");
        assertThat(saved.getLocation().getLat()).isEqualTo(37.2636);
        assertThat(saved.getLocation().getLng()).isEqualTo(127.0286);
        assertThat(saved.getLocation().getAdminLevel()).isEqualTo("경기도 수원시");
        assertThat(saved.getLocation().getZoom()).isEqualTo(14);
        assertThat(saved.getLocation().getLocationStatus()).isEqualTo("company");
        assertThat(saved.getLocation().getViewportNeLat()).isEqualTo(37.2700);
        assertThat(saved.getLocation().getViewportNeLng()).isEqualTo(127.0350);
        assertThat(saved.getLocation().getViewportSwLat()).isEqualTo(37.2570);
        assertThat(saved.getLocation().getViewportSwLng()).isEqualTo(127.0220);
        verify(mockItem).updateStatus(AnalysisStatus.COMPLETED);
    }

    @Test
    void processAnalysisResult_withoutLocation_savesNullLocation() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());

        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.87, 85, "HIGH", List.of(), List.of(), "분석 메시지", null
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);

        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        assertThat(captor.getValue().getLocation()).isNull();
    }

    @Test
    void processAnalysisResult_withLocationButNullViewport_savesNullViewportFields() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());

        AnalysisResponse.Location location = new AnalysisResponse.Location(
                "서울", 37.5665, 126.9780, "서울특별시", 12, "city", null, null
        );
        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.5, 50, "MEDIUM", List.of(), List.of(), "메시지", location
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);

        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        AIAnalysisResult saved = captor.getValue();
        assertThat(saved.getLocation()).isNotNull();
        assertThat(saved.getLocation().getRawText()).isEqualTo("서울");
        assertThat(saved.getLocation().getViewportNeLat()).isNull();
        assertThat(saved.getLocation().getViewportSwLat()).isNull();
    }

    @Test
    void processAnalysisResult_duplicateMessage_skipsAndDoesNotSave() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        AIAnalysisResult existing = mock(AIAnalysisResult.class);
        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.of(existing));

        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.87, 85, "HIGH", List.of(), List.of(), "메시지", null
        );

        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository, never()).save(any());
    }

    @Test
    void processAnalysisResult_nullRiskScore_savesFailedStatus() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());

        AnalysisResponse response = new AnalysisResponse(
                "12345", null, null, null, List.of(), List.of(), "AI 분석 실패", null
        );

        service.processAnalysisResult(response);

        verify(mockItem).updateStatus(AnalysisStatus.FAILED);
    }

    @Test
    void processAnalysisResult_salaryBelowMinWage_appliesPenaltyToScoreAndLevel() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        Country mockCountry = mock(Country.class);
        CountryMetricsSnapshot mockSnapshot = mock(CountryMetricsSnapshot.class);

        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());
        when(mockItem.getSalary()).thenReturn(BigDecimal.valueOf(10_000_000));   // 1천만 (최저 연봉 이하)
        when(mockItem.getCountry()).thenReturn(mockCountry);
        when(countryMetricsSnapshotRepository.findTopByCountryOrderBySnapshotDateDesc(mockCountry))
                .thenReturn(Optional.of(mockSnapshot));
        when(mockSnapshot.getMinWage()).thenReturn(BigDecimal.valueOf(2_000_000));  // 월 200만
        when(mockSnapshot.getMinWageUnit()).thenReturn(WageUnit.MONTHLY);           // 연환산 2400만

        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.3, 60, "MEDIUM", List.of(), List.of(), "메시지", null
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);
        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        AIAnalysisResult saved = captor.getValue();
        assertThat(saved.getRiskScore()).isEqualTo(90);       // 60 + 30 = 90
        assertThat(saved.getRiskLevel()).isEqualTo("HIGH");   // 90 >= 65 → HIGH
    }

    @Test
    void processAnalysisResult_salaryBelowMinWage_scoreCappedAt99() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        Country mockCountry = mock(Country.class);
        CountryMetricsSnapshot mockSnapshot = mock(CountryMetricsSnapshot.class);

        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());
        when(mockItem.getSalary()).thenReturn(BigDecimal.valueOf(1_000_000));
        when(mockItem.getCountry()).thenReturn(mockCountry);
        when(countryMetricsSnapshotRepository.findTopByCountryOrderBySnapshotDateDesc(mockCountry))
                .thenReturn(Optional.of(mockSnapshot));
        when(mockSnapshot.getMinWage()).thenReturn(BigDecimal.valueOf(2_000_000));
        when(mockSnapshot.getMinWageUnit()).thenReturn(WageUnit.MONTHLY);

        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.9, 20, "HIGH", List.of(), List.of(), "메시지", null
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);
        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        assertThat(captor.getValue().getRiskScore()).isEqualTo(50);  // 20 + 30 = 50
    }

    @Test
    void processAnalysisResult_salaryAboveMinWage_noPenaltyApplied() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        Country mockCountry = mock(Country.class);
        CountryMetricsSnapshot mockSnapshot = mock(CountryMetricsSnapshot.class);

        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());
        when(mockItem.getSalary()).thenReturn(BigDecimal.valueOf(50_000_000));  // 5천만
        when(mockItem.getCountry()).thenReturn(mockCountry);
        when(countryMetricsSnapshotRepository.findTopByCountryOrderBySnapshotDateDesc(mockCountry))
                .thenReturn(Optional.of(mockSnapshot));
        when(mockSnapshot.getMinWage()).thenReturn(BigDecimal.valueOf(2_000_000));
        when(mockSnapshot.getMinWageUnit()).thenReturn(WageUnit.MONTHLY);

        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.3, 80, "LOW", List.of(), List.of(), "메시지", null
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);
        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        AIAnalysisResult saved = captor.getValue();
        assertThat(saved.getRiskScore()).isEqualTo(80);
        assertThat(saved.getRiskLevel()).isEqualTo("LOW");
    }

    @Test
    void processAnalysisResult_snapshotNotFound_keepsOriginalValues() {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        Country mockCountry = mock(Country.class);

        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        when(aiAnalysisResultRepository.findByAnalysisItem(mockItem)).thenReturn(Optional.empty());
        when(mockItem.getSalary()).thenReturn(BigDecimal.valueOf(10_000_000));
        when(mockItem.getCountry()).thenReturn(mockCountry);
        when(countryMetricsSnapshotRepository.findTopByCountryOrderBySnapshotDateDesc(mockCountry))
                .thenReturn(Optional.empty());

        AnalysisResponse response = new AnalysisResponse(
                "12345", 0.3, 70, "LOW", List.of(), List.of(), "메시지", null
        );

        ArgumentCaptor<AIAnalysisResult> captor = ArgumentCaptor.forClass(AIAnalysisResult.class);
        service.processAnalysisResult(response);

        verify(aiAnalysisResultRepository).save(captor.capture());
        AIAnalysisResult saved = captor.getValue();
        assertThat(saved.getRiskScore()).isEqualTo(70);
        assertThat(saved.getRiskLevel()).isEqualTo("LOW");
    }

    @Test
    void processAnalysisRequest_publishFailure_marksAnalysisFailed() throws Exception {
        AnalysisItem mockItem = mock(AnalysisItem.class);
        Country country = mock(Country.class);
        when(mockItem.getId()).thenReturn(12345L);
        when(mockItem.getCountry()).thenReturn(country);
        when(country.getCode()).thenReturn("KR");
        when(country.getCurrencyCode()).thenReturn("KRW");
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"analysisItemId\":12345}");
        when(analysisRequestPublisher.publish(any()))
                .thenReturn(ApiFutures.immediateFailedFuture(new RuntimeException("publish failed")));
        when(analysisItemRepository.findById(12345L)).thenReturn(Optional.of(mockItem));
        doAnswer(invocation -> {
            Consumer<TransactionStatus> callback = invocation.getArgument(0);
            callback.accept(null);
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());

        service.processAnalysisRequest(mockItem, BigDecimal.valueOf(100000), List.of("https://example.com/image.png"));

        verify(mockItem).updateStatus(AnalysisStatus.PROCESSING);
        verify(mockItem).updateStatus(AnalysisStatus.FAILED);
    }
}
