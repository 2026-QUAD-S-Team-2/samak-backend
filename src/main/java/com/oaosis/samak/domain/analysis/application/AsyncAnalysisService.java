package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.domain.analysis.exception.AnalysisErrorCode;
import com.oaosis.samak.domain.analysis.exception.AnalysisException;
import com.oaosis.samak.domain.analysis.repository.AIAnalysisResultRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.infra.openFeign.ai.client.AIServerClient;
import com.oaosis.samak.infra.openFeign.ai.dto.request.AIAnalyzeRequest;
import com.oaosis.samak.infra.openFeign.ai.dto.response.AIAnalyzeResponse;
import com.oaosis.samak.infra.rabbitMQ.config.RabbitMQProperties;
import com.oaosis.samak.infra.rabbitMQ.dto.AIAnalysisMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncAnalysisService {

    private final AnalysisItemRepository analysisItemRepository;
    private final AIAnalysisResultRepository aiAnalysisResultRepository;
    private final AIServerClient aiServerClient;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    /**
     * RabbitMQ에 AI 분석 요청 메시지 발행 (Producer)
     */
    public void processAIAnalysisAfterCommit(Long analysisItemId, Country country, BigDecimal salary, List<String> imageUrls) {
        AIAnalysisMessage message = new AIAnalysisMessage(
                analysisItemId,
                country.getCode(),
                salary,
                imageUrls
        );

        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchange().getAnalysis(),
                rabbitMQProperties.getRoutingKey().getAnalysis(),
                message
        );
    }

    /**
     * AI 분석 처리 (Consumer)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAIAnalysis(Long analysisItemId, BigDecimal salary, List<String> imageUrls) {
        AnalysisItem analysisItem = analysisItemRepository.findById(analysisItemId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        analysisItem.updateStatus(AnalysisStatus.PROCESSING);
        analysisItemRepository.save(analysisItem);

        // AI 서버 분석 요청
        AIAnalyzeResponse aiAnalyzeResponse = aiServerClient.analyzeImage(
                AIAnalyzeRequest.of(analysisItem.getCountry(), salary, imageUrls)
        );

        AIAnalysisResult aiAnalysisResult = new AIAnalysisResult(
                analysisItem,
                aiAnalyzeResponse.riskScore(),
                aiAnalyzeResponse.riskLevel(),
                aiAnalyzeResponse.message()
        );
        aiAnalysisResultRepository.save(aiAnalysisResult);

        analysisItem.updateStatus(AnalysisStatus.COMPLETED);
        analysisItemRepository.save(analysisItem);
    }
}