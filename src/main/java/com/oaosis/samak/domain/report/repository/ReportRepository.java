package com.oaosis.samak.domain.report.repository;

import com.oaosis.samak.domain.report.entity.Report;
import com.oaosis.samak.domain.report.entity.ReportIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}