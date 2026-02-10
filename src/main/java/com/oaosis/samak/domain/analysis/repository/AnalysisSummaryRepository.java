package com.oaosis.samak.domain.analysis.repository;

import com.oaosis.samak.domain.analysis.entity.AnalysisSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisSummaryRepository extends JpaRepository<AnalysisSummary, Long> {
}