package com.oaosis.samak.domain.country.entity;

import com.oaosis.samak.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"country_id", "snapshot_date"}
                )
        }
)
public class CountryMetricsSnapshot{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_metrics_snapshot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "min_wage", precision = 12, scale = 2)
    private BigDecimal minWage;

    @Column(name = "median_annual_income", precision = 12, scale = 2)
    private BigDecimal medianAnnualIncome;

    @Column(name = "avg_annual_income", precision = 12, scale = 2)
    private BigDecimal avgAnnualIncome;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    public CountryMetricsSnapshot(Country country, BigDecimal minWage, BigDecimal medianAnnualIncome, BigDecimal avgAnnualIncome, LocalDate snapshotDate) {
        this.country = country;
        this.minWage = minWage;
        this.medianAnnualIncome = medianAnnualIncome;
        this.avgAnnualIncome = avgAnnualIncome;
        this.snapshotDate = snapshotDate;
    }
}