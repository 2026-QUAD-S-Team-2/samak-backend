package com.oaosis.samak.domain.report.repository;

import com.oaosis.samak.domain.report.entity.ReportIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportIdentifierRepository extends JpaRepository<ReportIdentifier, Long> {
}