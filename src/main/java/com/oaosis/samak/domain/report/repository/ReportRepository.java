package com.oaosis.samak.domain.report.repository;

import com.oaosis.samak.domain.report.dto.response.ReportListResponse;
import com.oaosis.samak.domain.report.entity.Report;
import com.oaosis.samak.global.entity.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("""
            SELECT new com.oaosis.samak.domain.report.dto.response.ReportListResponse(
                r.companyName, MAX(r.createdAt), COUNT(r))
            FROM Report r
            WHERE r.companyName LIKE %:keyword%
            GROUP BY r.companyName
            ORDER BY MAX(r.createdAt) DESC
            """)
    List<ReportListResponse> findByCompanyNameOrderByLatest(@Param("keyword") String keyword);

    @Query("""
            SELECT new com.oaosis.samak.domain.report.dto.response.ReportListResponse(
                r.companyName, MAX(r.createdAt), COUNT(r))
            FROM Report r
            WHERE r.companyName LIKE %:keyword%
            GROUP BY r.companyName
            ORDER BY COUNT(r) DESC
            """)
    List<ReportListResponse> findByCompanyNameOrderByCount(@Param("keyword") String keyword);

    @Query("""
            SELECT new com.oaosis.samak.domain.report.dto.response.ReportListResponse(
                r.companyName, MAX(r.createdAt), COUNT(r))
            FROM Report r JOIN ReportIdentifier ri ON ri.report = r
            WHERE ri.type = :type AND ri.value = :keyword
            GROUP BY r.companyName
            ORDER BY MAX(r.createdAt) DESC
            """)
    List<ReportListResponse> findByIdentifierOrderByLatest(
            @Param("type") ContactType type,
            @Param("keyword") String keyword
    );

    @Query("""
            SELECT new com.oaosis.samak.domain.report.dto.response.ReportListResponse(
                r.companyName, MAX(r.createdAt), COUNT(r))
            FROM Report r JOIN ReportIdentifier ri ON ri.report = r
            WHERE ri.type = :type AND ri.value = :keyword
            GROUP BY r.companyName
            ORDER BY COUNT(r) DESC
            """)
    List<ReportListResponse> findByIdentifierOrderByCount(
            @Param("type") ContactType type,
            @Param("keyword") String keyword
    );
}