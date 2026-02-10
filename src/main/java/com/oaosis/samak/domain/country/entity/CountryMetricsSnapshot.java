package com.oaosis.samak.domain.country.entity;

import com.oaosis.samak.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountryMetricsSnapshot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_metrics_snapshot_id")
    private Long id;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "min_wage", precision = 12, scale = 2)
    private BigDecimal minWage;

    @Column(name = "avg_income", precision = 12, scale = 2)
    private BigDecimal avgIncome;
}