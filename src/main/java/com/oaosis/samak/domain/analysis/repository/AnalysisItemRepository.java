package com.oaosis.samak.domain.analysis.repository;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisItemRepository extends JpaRepository<AnalysisItem, Long> {
}