package com.oaosis.samak.infra.rabbitMQ.service;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.domain.analysis.exception.AnalysisErrorCode;
import com.oaosis.samak.domain.analysis.exception.AnalysisException;
import com.oaosis.samak.domain.analysis.repository.AIAnalysisResultRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.infra.rabbitMQ.dto.request.AnalysisRequest;
import com.oaosis.samak.infra.rabbitMQ.dto.response.AnalysisResponse;
import com.oaosis.samak.infra.rabbitMQ.config.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncAnalysisService {

    private final AnalysisItemRepository analysisItemRepository;
    private final AIAnalysisResultRepository aiAnalysisResultRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    /**
     * AI 분석 요청 메시지 발행 (Producer)
     */
    @Transactional
    public void processAnalysisRequest(AnalysisItem analysisItem, BigDecimal salary, List<String> imageUrls) {
        analysisItem.updateStatus(AnalysisStatus.PROCESSING);
        AnalysisRequest message = AnalysisRequest.of(analysisItem.getId(), analysisItem.getCountry(), salary, imageUrls);

        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange().getAnalysisRequest(),
                rabbitMQProperties.getRoutingKey().getAnalysisRequest(),
                message
        );
    }

    /**
    * AI 분석 결과 메시지 처리 (Consumer)
    */
    @Transactional
    public void processAnalysisResult(AnalysisResponse message) {
        AnalysisItem analysisItem = analysisItemRepository.findById(Long.valueOf(message.analysisId()))
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        AIAnalysisResult aiAnalysisResult = new AIAnalysisResult(
                analysisItem,
                message.riskScore(),
                message.riskLevel(),
                message.message());
        aiAnalysisResultRepository.save(aiAnalysisResult);

        analysisItem.updateStatus(AnalysisStatus.COMPLETED);
    }

    /**
     * AI 분석 요청 실패 메시지 처리 (Consumer)
     */
    @Transactional
    public void processAnalysisFailure(Long analysisItemId) {
        AnalysisItem analysisItem = analysisItemRepository.findById(analysisItemId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        analysisItem.updateStatus(AnalysisStatus.FAILED);
    }
}