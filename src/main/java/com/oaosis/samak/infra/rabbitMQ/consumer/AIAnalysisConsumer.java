package com.oaosis.samak.infra.rabbitMQ.consumer;

import com.oaosis.samak.domain.analysis.application.AsyncAnalysisService;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.exception.AnalysisErrorCode;
import com.oaosis.samak.domain.analysis.exception.AnalysisException;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.infra.rabbitMQ.config.RabbitMQProperties;
import com.oaosis.samak.infra.rabbitMQ.dto.AIAnalysisMessage;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.oaosis.samak.domain.analysis.enums.AnalysisStatus.FAILED;

/**
 * AI 분석 RabbitMQ Consumer
 * - Retry Queue를 이용한 재시도 로직
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AIAnalysisConsumer {

    private final AsyncAnalysisService asyncAnalysisService;
    private final AnalysisItemRepository analysisItemRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private static final int MAX_RETRIES = 1;
    private static final String RETRY_COUNT_HEADER = "x-retry-count";


    @RabbitListener(queues = "${rabbitmq.queue.analysis}", ackMode = "MANUAL")
    public void handleAIAnalysisMessage(
            AIAnalysisMessage message,
            Message amqpMessage,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(name = RETRY_COUNT_HEADER, required = false) Integer retryCountHeader) throws IOException {

        int retryCount = (retryCountHeader != null) ? retryCountHeader : 0;

        try {
            asyncAnalysisService.processAIAnalysis(
                    message.getAnalysisItemId(),
                    message.getSalary(),
                    message.getImageUrls()
            );

            // 성공 시 ACK
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            if (retryCount >= MAX_RETRIES) {
                log.error("*** Max retries reached. Moving to DLQ: {} ***", message.getAnalysisItemId());
                // requeue = false로 설정하여 설정된 DLX(ai.analysis.dlx)로 전송
                channel.basicNack(deliveryTag, false, false);
            } else {
                log.warn("*** Retrying ({}/{}): {} ***", retryCount + 1, MAX_RETRIES, message.getAnalysisItemId());

                MessageProperties props = amqpMessage.getMessageProperties();
                props.setHeader(RETRY_COUNT_HEADER, retryCount + 1);

                rabbitTemplate.send(
                        rabbitMQProperties.getExchange().getAnalysisRetry(),
                        rabbitMQProperties.getRoutingKey().getAnalysisRetry(),
                        amqpMessage
                );

                channel.basicAck(deliveryTag, false);
            }
        }
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.analysisDLQ}")
    public void handleFailedAnalysisMessage(
            AIAnalysisMessage message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        log.error("*** Analysis Message in DLQ - analysisItemId: {} ***", message.getAnalysisItemId());

        AnalysisItem analysisItem = analysisItemRepository.findById(
                message.getAnalysisItemId()).orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        analysisItem.updateStatus(FAILED);

        channel.basicAck(deliveryTag, false);
    }
}