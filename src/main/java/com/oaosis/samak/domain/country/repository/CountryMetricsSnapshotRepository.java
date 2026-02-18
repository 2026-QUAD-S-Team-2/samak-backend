package com.oaosis.samak.domain.country.repository;

import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.domain.country.entity.CountryMetricsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryMetricsSnapshotRepository extends JpaRepository<CountryMetricsSnapshot, Long> {
    Optional<CountryMetricsSnapshot> findTopByCountryOrderBySnapshotDateDesc(Country country);

}