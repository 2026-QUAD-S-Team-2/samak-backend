package com.oaosis.samak.domain.analysis.repository;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnalysisItemRepository extends JpaRepository<AnalysisItem, Long> {

    @Query("""
            SELECT DISTINCT ai FROM AnalysisItem ai
            LEFT JOIN FETCH ai.analysisSummary
            WHERE ai.member = :member
            ORDER BY ai.createdAt DESC
            """)
    List<AnalysisItem> findByUserWithSummaryOrderByCreatedAtDesc(@Param("member") Member member);

    @Query("""
            SELECT DISTINCT ai FROM AnalysisItem ai
            LEFT JOIN FETCH ai.analysisSummary
            LEFT JOIN FETCH ai.countryWarning
            WHERE ai.id = :analysisItemId
            """)
    Optional<AnalysisItem> findByIdWithDetails(@Param("analysisItemId") Long analysisItemId);
}