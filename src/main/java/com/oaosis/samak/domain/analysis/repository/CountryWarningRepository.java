package com.oaosis.samak.domain.analysis.repository;

import com.oaosis.samak.domain.analysis.entity.CountryWarning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryWarningRepository extends JpaRepository<CountryWarning, Long> {
}