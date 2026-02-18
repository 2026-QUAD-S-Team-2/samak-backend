package com.oaosis.samak.domain.analysis.entity;

import com.oaosis.samak.global.entity.ContactType;
import com.oaosis.samak.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisItemIdentifier extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_item_identifier_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_item_id", nullable = false)
    private AnalysisItem analysisItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactType type;

    @Column(nullable = false)
    private String value;
}