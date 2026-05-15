package com.oaosis.samak.infra.pubsub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.entity.LocationInfo;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.domain.analysis.exception.AnalysisErrorCode;
import com.oaosis.samak.domain.analysis.exception.AnalysisException;
import com.oaosis.samak.domain.analysis.repository.AIAnalysisResultRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.infra.pubsub.dto.request.AnalysisRequest;
import com.oaosis.samak.infra.pubsub.dto.response.AnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PubSubAsyncAnalysisService {

    private final AnalysisItemRepository analysisItemRepository;
    private final AIAnalysisResultRepository aiAnalysisResultRepository;
    private final Publisher analysisRequestPublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processAnalysisRequest(AnalysisItem analysisItem, BigDecimal salary, List<String> imageUrls) {
        analysisItem.updateStatus(AnalysisStatus.PROCESSING);
        AnalysisRequest message = AnalysisRequest.of(analysisItem.getId(), analysisItem.getCountry(), salary, imageUrls);

        try {
            String json = objectMapper.writeValueAsString(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(ByteString.copyFromUtf8(json))
                    .build();

            ApiFutures.addCallback(
                    analysisRequestPublisher.publish(pubsubMessage),
                    new ApiFutureCallback<>() {
                        @Override
                        public void onSuccess(String messageId) {
                            log.info("Published analysis request for item {}, messageId: {}", analysisItem.getId(), messageId);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            log.error("Failed to publish analysis request for item {}: {}", analysisItem.getId(), t.getMessage());
                        }
                    },
                    MoreExecutors.directExecutor()
            );
        } catch (Exception e) {
            log.error("Failed to serialize analysis request for item {}: {}", analysisItem.getId(), e.getMessage());
            throw new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND);
        }
    }

    @Transactional
    public void processAnalysisResult(AnalysisResponse message) {
        AnalysisItem analysisItem = analysisItemRepository.findById(Long.valueOf(message.analysisId()))
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        if (aiAnalysisResultRepository.findByAnalysisItem(analysisItem).isPresent()) {
            log.warn("Duplicate message for analysisItemId: {}, skipping", analysisItem.getId());
            return;
        }

        if (message.riskScore() == null) {
            aiAnalysisResultRepository.save(new AIAnalysisResult(analysisItem, message.message()));
            analysisItem.updateStatus(AnalysisStatus.FAILED);
            return;
        }

        aiAnalysisResultRepository.save(new AIAnalysisResult(
                analysisItem,
                message.riskScore(),
                message.riskLevel(),
                message.message(),
                buildLocationInfo(message.location())));
        analysisItem.updateStatus(AnalysisStatus.COMPLETED);
    }

    private LocationInfo buildLocationInfo(AnalysisResponse.Location loc) {
        if (loc == null) {
            return null;
        }
        Double neLat = Optional.ofNullable(loc.viewportNe()).map(AnalysisResponse.Location.ViewportPoint::lat).orElse(null);
        Double neLng = Optional.ofNullable(loc.viewportNe()).map(AnalysisResponse.Location.ViewportPoint::lng).orElse(null);
        Double swLat = Optional.ofNullable(loc.viewportSw()).map(AnalysisResponse.Location.ViewportPoint::lat).orElse(null);
        Double swLng = Optional.ofNullable(loc.viewportSw()).map(AnalysisResponse.Location.ViewportPoint::lng).orElse(null);
        return new LocationInfo(loc.rawText(), loc.lat(), loc.lng(), loc.adminLevel(), loc.zoom(), loc.status(), neLat, neLng, swLat, swLng);
    }

    @Transactional
    public void processAnalysisFailure(Long analysisItemId) {
        AnalysisItem analysisItem = analysisItemRepository.findById(analysisItemId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        analysisItem.updateStatus(AnalysisStatus.FAILED);
    }
}
