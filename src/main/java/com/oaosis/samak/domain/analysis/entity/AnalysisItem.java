package com.oaosis.samak.domain.analysis.entity;

import com.oaosis.samak.domain.analysis.entity.enums.AnalysisItemType;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisItemType type;

    @Column(name = "source_url", columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(name = "raw_text", nullable = false, columnDefinition = "TEXT")
    private String rawText;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(precision = 12, scale = 2)
    private BigDecimal salary;
}