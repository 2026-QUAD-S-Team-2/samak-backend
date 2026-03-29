package com.oaosis.samak.infra.rabbitMQ.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;
    private final String DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private final String DEAD_LETTER_KEY = "x-dead-letter-routing-key";
    private final String MESSAGE_TTL = "x-message-ttl";

    // === Analysis Queue & Exchange Configuration ===
    @Bean
    public DirectExchange analysisRequestExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisRequest(), true, false);
    }

    @Bean
    public Queue analysisRequestQueue() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisRequest())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getExchange().getAnalysisRequestDLX())
                .withArgument(DEAD_LETTER_KEY, rabbitMQProperties.getRoutingKey().getAnalysisRequestDLQ())
                .build();
    }

    @Bean
    public Binding analysisRequestBinding() {
        return BindingBuilder
                .bind(analysisRequestQueue())
                .to(analysisRequestExchange())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisRequest());
    }


    // === Analysis Retry Queue & Exchange Configuration ===
    @Bean
    public DirectExchange analysisRetryExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisRetry(), true, false);
    }

    @Bean
    public Queue analysisRetryQueue() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisRetry())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getExchange().getAnalysisRequest())
                .withArgument(DEAD_LETTER_KEY, rabbitMQProperties.getRoutingKey().getAnalysisRequest())
                .withArgument(MESSAGE_TTL, 5000)
                .build();
    }

    @Bean
    public Binding analysisRetryBinding() {
        return BindingBuilder
                .bind(analysisRetryQueue())
                .to(analysisRetryExchange())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisRetry());
    }


    // === Analysis Request DLX & DLQ Configuration ===
    @Bean
    public DirectExchange analysisRequestDLX() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisRequestDLX(), true, false);
    }

    @Bean
    public Queue analysisRequestDLQ() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisRequestDLQ()).build();
    }

    @Bean
    public Binding analysisRequestDLQBinding() {
        return BindingBuilder
                .bind(analysisRequestDLQ())
                .to(analysisRequestDLX())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisRequestDLQ());
    }

    // === Analysis Result DLX & DLQ Configuration ===
    @Bean
    public DirectExchange analysisResultDLX() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisResultDLX(), true, false);
    }

    @Bean
    public Queue analysisResultDLQ() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisResultDLQ()).build();
    }

    @Bean
    public Binding analysisResultDLQBinding() {
        return BindingBuilder
                .bind(analysisResultDLQ())
                .to(analysisResultDLX())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisResultDLQ());
    }


    // === Analysis Result Queue & Exchange Configuration ===
    @Bean
    public DirectExchange analysisResultExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisResult(), true, false);
    }

    @Bean
    public Queue analysisResultQueue() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisResult())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getExchange().getAnalysisResultDLX())
                .withArgument(DEAD_LETTER_KEY, rabbitMQProperties.getRoutingKey().getAnalysisResultDLQ())
                .build();
    }

    @Bean
    public Binding analysisResultBinding() {
        return BindingBuilder
                .bind(analysisResultQueue())
                .to(analysisResultExchange())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisResult());
    }

    // === RabbitTemplate Configuration with JSON Message Converter ===
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}