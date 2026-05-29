package com.oaosis.samak.domain.report.repository;

import com.oaosis.samak.domain.report.entity.EvidenceImage;
import com.oaosis.samak.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceImageRepository extends JpaRepository<EvidenceImage, Long> {
    List<EvidenceImage> findAllByReportIn(List<Report> reports);
}
