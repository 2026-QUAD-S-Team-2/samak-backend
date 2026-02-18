package com.oaosis.samak.domain.analysis.entity;

import com.oaosis.samak.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountryWarning extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_warning_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_item_id", nullable = false)
    private AnalysisItem analysisItem;

    @Column(name = "warning_message", nullable = false, columnDefinition = "TEXT")
    private String warningMessage;

    public CountryWarning(AnalysisItem analysisItem, String warningMessage) {
        this.analysisItem = analysisItem;
        this.warningMessage = warningMessage;
    }
}