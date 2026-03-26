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

    @Bean
    public DirectExchange aiAnalysisExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysis(), true, false);
    }

    @Bean
    public Queue aiAnalysisQueue() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysis())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getExchange().getAnalysisDLX())
                .withArgument(DEAD_LETTER_KEY, rabbitMQProperties.getRoutingKey().getAnalysisDLQ())
                .build();
    }

    @Bean
    public Binding aiAnalysisBinding() {
        return BindingBuilder
                .bind(aiAnalysisQueue())
                .to(aiAnalysisExchange())
                .with(rabbitMQProperties.getRoutingKey().getAnalysis());
    }

    @Bean
    public DirectExchange aiAnalysisRetryExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisRetry(), true, false);
    }

    @Bean
    public Queue aiAnalysisRetryQueue() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisRetry())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getExchange().getAnalysis())
                .withArgument(DEAD_LETTER_KEY, rabbitMQProperties.getRoutingKey().getAnalysis())
                .withArgument(MESSAGE_TTL, 5000)
                .build();
    }

    @Bean
    public Binding aiAnalysisRetryBinding() {
        return BindingBuilder
                .bind(aiAnalysisRetryQueue())
                .to(aiAnalysisRetryExchange())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisRetry());
    }

    @Bean
    public DirectExchange aiAnalysisDLX() {
        return new DirectExchange(rabbitMQProperties.getExchange().getAnalysisDLX(), true, false);
    }

    @Bean
    public Queue aiAnalysisDLQ() {
        return QueueBuilder.durable(rabbitMQProperties.getQueue().getAnalysisDLQ()).build();
    }

    @Bean
    public Binding aiAnalysisDLQBinding() {
        return BindingBuilder
                .bind(aiAnalysisDLQ())
                .to(aiAnalysisDLX())
                .with(rabbitMQProperties.getRoutingKey().getAnalysisDLQ());
    }

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