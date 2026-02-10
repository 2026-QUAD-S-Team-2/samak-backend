package com.oaosis.samak.domain.analysis.repository;

import com.oaosis.samak.domain.analysis.entity.AnalysisItemIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisItemIdentifierRepository extends JpaRepository<AnalysisItemIdentifier, Long> {
}