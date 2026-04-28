package com.oaosis.samak.infra.pubsub.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.oaosis.samak.infra.pubsub.config.PubSubProperties;
import com.oaosis.samak.infra.pubsub.dto.request.AnalysisRequest;
import com.oaosis.samak.infra.pubsub.dto.response.AnalysisResponse;
import com.oaosis.samak.infra.pubsub.service.PubSubAsyncAnalysisService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisResultSubscriber {

    private final PubSubProperties properties;
    private final PubSubAsyncAnalysisService pubSubAsyncAnalysisService;
    private final ObjectMapper objectMapper;

    private Subscriber resultSubscriber;
    private Subscriber dlrSubscriber;

    @PostConstruct
    public void startSubscribers() {
        resultSubscriber = buildSubscriber(
                properties.getSubscription().getAnalysisResult(),
                this::handleAnalysisResult
        );
        resultSubscriber.startAsync().awaitRunning();
        log.info("Analysis result subscriber started");

        dlrSubscriber = buildSubscriber(
                properties.getSubscription().getAnalysisRequestDlr(),
                this::handleFailedAnalysisRequest
        );
        dlrSubscriber.startAsync().awaitRunning();
        log.info("Analysis DLR subscriber started");
    }

    private Subscriber buildSubscriber(String subscriptionId, MessageReceiver receiver) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
                properties.getProjectId(), subscriptionId
        );
        Subscriber.Builder builder = Subscriber.newBuilder(subscriptionName, receiver);

        String credentialsJson = properties.getCredentialsJson();
        if (credentialsJson != null && !credentialsJson.isBlank()) {
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))
                );
                builder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to load Pub/Sub credentials", e);
            }
        }

        return builder.build();
    }

    private void handleAnalysisResult(PubsubMessage message, AckReplyConsumer consumer) {
        try {
            AnalysisResponse response = objectMapper.readValue(
                    message.getData().toStringUtf8(), AnalysisResponse.class
            );
            pubSubAsyncAnalysisService.processAnalysisResult(response);
            consumer.ack();
        } catch (Exception e) {
            log.error("Failed to process analysis result message: {}", e.getMessage());
            consumer.nack();
        }
    }

    private void handleFailedAnalysisRequest(PubsubMessage message, AckReplyConsumer consumer) {
        try {
            AnalysisRequest request = objectMapper.readValue(
                    message.getData().toStringUtf8(), AnalysisRequest.class
            );
            log.error("*** Analysis request failed for analysisItemId: {}. Handling failure. ***", request.analysisItemId());
            pubSubAsyncAnalysisService.processAnalysisFailure(request.analysisItemId());
            consumer.ack();
        } catch (Exception e) {
            log.error("Failed to process DLR message: {}", e.getMessage());
            consumer.nack();
        }
    }

    @PreDestroy
    public void stopSubscribers() {
        if (resultSubscriber != null) resultSubscriber.stopAsync();
        if (dlrSubscriber != null) dlrSubscriber.stopAsync();
    }
}
