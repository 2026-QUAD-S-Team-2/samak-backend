package com.oaosis.samak.infra.rabbitMQ.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 설정값 관리
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private Exchange exchange = new Exchange();
    private Queue queue = new Queue();
    private RoutingKey routingKey = new RoutingKey();

    @Getter
    @Setter
    public static class Exchange {
        private String analysis;
        private String analysisRetry;
        private String analysisDLX;
    }

    @Getter
    @Setter
    public static class Queue {
        private String analysis;
        private String analysisRetry;
        private String analysisDLQ;
    }

    @Getter
    @Setter
    public static class RoutingKey {
        private String analysis;
        private String analysisRetry;
        private String analysisDLQ;
    }
}