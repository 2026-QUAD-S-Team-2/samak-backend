package com.oaosis.samak.infra.pubsub.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class PubSubConfig {

    private final PubSubProperties properties;

    @Bean(destroyMethod = "shutdown")
    public Publisher analysisRequestPublisher() throws IOException {
        TopicName topicName = TopicName.of(properties.getProjectId(), properties.getTopic().getAnalysisRequest());
        Publisher.Builder builder = Publisher.newBuilder(topicName);

        String credentialsJson = properties.getCredentialsJson();
        if (credentialsJson != null && !credentialsJson.isBlank()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))
            );
            builder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
        }

        return builder.build();
    }
}
