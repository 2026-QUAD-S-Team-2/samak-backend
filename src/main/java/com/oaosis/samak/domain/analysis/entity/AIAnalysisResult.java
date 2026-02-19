package com.oaosis.samak.domain.analysis.entity;

import com.oaosis.samak.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "ai_analysis_result")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AIAnalysisResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_analysis_result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_item_id", nullable = false)
    private AnalysisItem analysisItem;

    @Column(name = "risk_score", nullable = false)
    private int riskScore;

    @Column(name="risk_level", nullable = false)
    private String riskLevel;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    public AIAnalysisResult(AnalysisItem analysisItem, int riskScore, String riskLevel, String message) {
        this.analysisItem = analysisItem;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.message = message;
    }
}