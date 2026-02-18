package com.oaosis.samak.domain.analysis.repository;

import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AIAnalysisResultRepository extends JpaRepository<AIAnalysisResult, Long> {
    Optional<AIAnalysisResult> findByAnalysisItem(AnalysisItem analysisItem);
}