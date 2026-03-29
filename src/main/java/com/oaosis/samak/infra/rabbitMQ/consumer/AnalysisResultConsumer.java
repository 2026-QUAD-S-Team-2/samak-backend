package com.oaosis.samak.infra.rabbitMQ.consumer;

import com.oaosis.samak.infra.rabbitMQ.service.AsyncAnalysisService;
import com.oaosis.samak.infra.rabbitMQ.dto.request.AnalysisRequest;
import com.oaosis.samak.infra.rabbitMQ.dto.response.AnalysisResponse;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisResultConsumer {

    private final AsyncAnalysisService asyncAnalysisService;

    @RabbitListener(queues = "${rabbitmq.queue.analysisResult}", ackMode = "MANUAL")
    public void handleAIAnalysisMessage(
            AnalysisResponse message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
            asyncAnalysisService.processAnalysisResult(message);
            channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(queues = "${rabbitmq.queue.analysisRequestDLQ}", ackMode = "MANUAL")
    public void handleFailedAnalysisRequestMessage(
            AnalysisRequest message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.error("*** Analysis request failed for analysisItemId: {}. Handling failure. ***", message.analysisItemId());

        asyncAnalysisService.processAnalysisFailure(message.analysisItemId());
        channel.basicAck(deliveryTag, false);
    }
}