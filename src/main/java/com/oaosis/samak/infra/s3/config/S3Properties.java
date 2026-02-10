package com.oaosis.samak.infra.s3.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloud.aws.default-bucket")
public class S3Properties {

    private Credentials credentials;
    private Region region;
    private S3 s3;

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static class Region {
        private String staticRegion;
    }

    @Getter
    @Setter
    public static class S3 {
        private String bucket;
        private String urlPrefix;
        private Paths paths;
    }

    @Getter
    @Setter
    public static class Paths {
        private String defaultProfileImage;
    }
}