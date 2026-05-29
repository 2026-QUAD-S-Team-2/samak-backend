package com.oaosis.samak.domain.report.repository;

import com.oaosis.samak.domain.report.entity.ReportIdentifier;
import com.oaosis.samak.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportIdentifierRepository extends JpaRepository<ReportIdentifier, Long> {
    List<ReportIdentifier> findAllByReportIn(List<Report> reports);
}
