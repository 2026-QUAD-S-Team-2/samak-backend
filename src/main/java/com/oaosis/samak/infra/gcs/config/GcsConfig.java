package com.oaosis.samak.infra.gcs.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class GcsConfig {
    private final GcsProperties gcsProperties;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = resolveCredentials();
        StorageOptions.Builder builder = StorageOptions.newBuilder()
                .setProjectId(gcsProperties.getProjectId());
        if (credentials != null) {
            builder.setCredentials(credentials);
        }
        return builder.build().getService();
    }

    private GoogleCredentials resolveCredentials() throws IOException {
        String location = gcsProperties.getCredentialsLocation();
        if (location != null && !location.isBlank()) {
            return GoogleCredentials.fromStream(new FileInputStream(location));
        }
        String json = gcsProperties.getCredentialsJson();
        if (json != null && !json.isBlank()) {
            return GoogleCredentials.fromStream(
                    new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))
            );
        }
        return null; // Application Default Credentials 사용
    }
}
