package com.oaosis.samak.domain.report.application;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.domain.report.dto.request.ReportCreateRequest;
import com.oaosis.samak.domain.report.dto.response.ReportCreateResponse;
import com.oaosis.samak.domain.report.dto.response.ReportHistoryResponse;
import com.oaosis.samak.domain.report.dto.response.ReportListResponse;
import com.oaosis.samak.domain.report.entity.EvidenceImage;
import com.oaosis.samak.domain.report.entity.Report;
import com.oaosis.samak.domain.report.entity.ReportIdentifier;
import com.oaosis.samak.domain.report.enums.ReportSearchType;
import com.oaosis.samak.domain.report.enums.ReportSortType;
import com.oaosis.samak.domain.report.repository.EvidenceImageRepository;
import com.oaosis.samak.domain.report.repository.ReportIdentifierRepository;
import com.oaosis.samak.domain.report.repository.ReportRepository;
import com.oaosis.samak.global.entity.ContactType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final EvidenceImageRepository evidenceImageRepository;
    private final ReportIdentifierRepository reportIdentifierRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReportCreateResponse createReport(String email, ReportCreateRequest request) {
        Member reporter = getMember(email);

        Report report = Report.builder()
                .reporter(reporter)
                .companyName(request.companyName())
                .reason(request.reason())
                .evidence(request.evidence())
                .build();
        reportRepository.save(report);

        if (request.imageNames() != null && !request.imageNames().isEmpty()) {
            List<EvidenceImage> images = request.imageNames().stream()
                    .map(name -> EvidenceImage.builder()
                            .report(report)
                            .imageName(name)
                            .build())
                    .toList();
            evidenceImageRepository.saveAll(images);
        }

        if (request.contactType() != null && request.contactValue() != null) {
            ReportIdentifier identifier = ReportIdentifier.builder()
                    .report(report)
                    .type(request.contactType())
                    .value(request.contactValue())
                    .build();
            reportIdentifierRepository.save(identifier);
        }

        return ReportCreateResponse.from(report);
    }

    public List<ReportListResponse> getReportList(ReportSearchType searchType, ReportSortType sortType, String keyword) {
        if (searchType == ReportSearchType.COMPANY_NAME) {
            return sortType == ReportSortType.LATEST
                    ? reportRepository.findByCompanyNameOrderByLatest(keyword)
                    : reportRepository.findByCompanyNameOrderByCount(keyword);
        }

        ContactType contactType = switch (searchType) {
            case EMAIL -> ContactType.EMAIL;
            case TELEGRAM -> ContactType.TELEGRAM;
            case PHONE -> ContactType.PHONE;
            default -> throw new IllegalArgumentException("지원하지 않는 검색 유형입니다.");
        };

        return sortType == ReportSortType.LATEST
                ? reportRepository.findByIdentifierOrderByLatest(contactType, keyword)
                : reportRepository.findByIdentifierOrderByCount(contactType, keyword);
    }

    public List<ReportHistoryResponse> getReportHistory(
            String companyName, ContactType identifierType, String identifierValue) {
        LinkedHashSet<ReportHistoryResponse> merged = new LinkedHashSet<>();

        if (companyName != null && !companyName.isBlank()) {
            merged.addAll(reportRepository.findHistoryByCompanyName(companyName));
        }

        if (identifierType != null && identifierValue != null && !identifierValue.isBlank()) {
            merged.addAll(reportRepository.findHistoryByIdentifier(identifierType, identifierValue));
        }

        return merged.stream()
                .sorted(Comparator.comparing(ReportHistoryResponse::reportedAt).reversed())
                .toList();
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}