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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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

    @Async("aiAnalysisExecutor")
    public void processAIAnalysisAfterCommit(Long analysisItemId, Country country, BigDecimal salary, List<String> imageNames) {
        // 100ms 지연을 통한 트랜잭션 커밋 완료 보장
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        processAIAnalysis(analysisItemId, country, salary, imageNames);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processAIAnalysis(Long analysisItemId, Country country, BigDecimal salary, List<String> imageNames) {
        log.info("** Start AI Analysis - analysisItemId: {} **", analysisItemId);

        AnalysisItem analysisItem = analysisItemRepository.findById(analysisItemId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));

        try {
            analysisItem.updateStatus(AnalysisStatus.PROCESSING);
            analysisItemRepository.save(analysisItem);

            // AI 서버 분석 요청
            AIAnalyzeResponse aiAnalyzeResponse = aiServerClient.analyzeImage(AIAnalyzeRequest.of(country, salary, imageNames));

            AIAnalysisResult aiAnalysisResult = new AIAnalysisResult(
                    analysisItem,
                    aiAnalyzeResponse.riskScore(),
                    aiAnalyzeResponse.riskLevel(),
                    aiAnalyzeResponse.message()
            );
            aiAnalysisResultRepository.save(aiAnalysisResult);

            analysisItem.updateStatus(AnalysisStatus.COMPLETED);
            analysisItemRepository.save(analysisItem);

            log.info("** Completed AI Analysis - analysisItemId: {} **", analysisItemId);
        } catch (Exception e) {
            log.error("** AI Analysis Failed - analysisItemId: {} - Error: {} **", analysisItemId, e.getMessage(), e);

            analysisItem.updateStatus(AnalysisStatus.FAILED);

            AIAnalysisResult aiAnalysisResult = new AIAnalysisResult(analysisItem, e.getMessage());
            aiAnalysisResultRepository.save(aiAnalysisResult);
            analysisItemRepository.save(analysisItem);
        }
    }
}