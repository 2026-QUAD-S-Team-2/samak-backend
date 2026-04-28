package com.oaosis.samak.infra.pubsub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pubsub")
public class PubSubProperties {

    private String projectId;
    private String credentialsJson;
    private Topic topic = new Topic();
    private Subscription subscription = new Subscription();

    @Getter
    @Setter
    public static class Topic {
        private String analysisRequest;
        private String analysisRequestDlr;
    }

    @Getter
    @Setter
    public static class Subscription {
        private String analysisResult;
        private String analysisRequestDlr;
    }
}
