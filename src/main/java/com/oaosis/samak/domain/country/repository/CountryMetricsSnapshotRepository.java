package com.oaosis.samak.domain.country.repository;

import com.oaosis.samak.domain.country.entity.CountryMetricsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryMetricsSnapshotRepository extends JpaRepository<CountryMetricsSnapshot, Long> {
}