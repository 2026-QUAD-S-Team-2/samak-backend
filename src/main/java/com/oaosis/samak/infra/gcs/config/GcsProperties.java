package com.oaosis.samak.infra.gcs.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloud.gcs")
public class GcsProperties {
    private String projectId;
    private String credentialsLocation;  // JSON 파일 경로
    private String credentialsJson;      // JSON 내용 (환경변수 등)
    private String bucket;
    private String urlPrefix;
    private Paths paths;

    @Getter
    @Setter
    public static class Paths {
        private String defaultProfileImage;
    }
}
