package com.oaosis.samak.domain.report.application;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.domain.report.dto.request.ReportCreateRequest;
import com.oaosis.samak.domain.report.dto.response.ReportCreateResponse;
import com.oaosis.samak.domain.report.dto.response.ReportDetailResponse;
import com.oaosis.samak.domain.report.dto.response.ReportHistoryResponse;
import com.oaosis.samak.domain.report.dto.response.ReportListResponse;
import com.oaosis.samak.domain.report.entity.EvidenceImage;
import com.oaosis.samak.domain.report.entity.Report;
import com.oaosis.samak.domain.report.entity.ReportIdentifier;
import com.oaosis.samak.domain.report.enums.ReportSearchType;
import com.oaosis.samak.domain.report.enums.ReportSortType;
import com.oaosis.samak.domain.report.exception.ReportErrorCode;
import com.oaosis.samak.domain.report.exception.ReportException;
import com.oaosis.samak.domain.report.repository.EvidenceImageRepository;
import com.oaosis.samak.domain.report.repository.ReportIdentifierRepository;
import com.oaosis.samak.domain.report.repository.ReportRepository;
import com.oaosis.samak.global.entity.ContactType;
import com.oaosis.samak.infra.gcs.service.GcsUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final EvidenceImageRepository evidenceImageRepository;
    private final ReportIdentifierRepository reportIdentifierRepository;
    private final MemberRepository memberRepository;
    private final GcsUrlBuilder gcsUrlBuilder;

    @Transactional
    public ReportCreateResponse createReport(String email, ReportCreateRequest request) {
        Member reporter = getMember(email);

        Report report = Report.builder()
                .reporter(reporter)
                .companyName(request.companyName())
                .reason(request.reason())
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
            case KAKAO_TALK -> ContactType.KAKAO_TALK;
            case MESSAGE -> ContactType.MESSAGE;
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

    public ReportDetailResponse getReportDetail(String companyName) {
        List<Report> reports = reportRepository.findAllByCompanyNameWithReporterOrderByCreatedAtDesc(companyName);
        if (reports.isEmpty()) {
            throw new ReportException(ReportErrorCode.REPORT_NOT_FOUND);
        }

        Map<ContactType, LinkedHashSet<String>> contactMethodValues = new LinkedHashMap<>();
        reportIdentifierRepository.findAllByReportIn(reports).forEach(identifier ->
                contactMethodValues
                        .computeIfAbsent(identifier.getType(), ignored -> new LinkedHashSet<>())
                        .add(identifier.getValue())
        );
        List<ReportDetailResponse.ContactMethodResponse> contactMethods = contactMethodValues.entrySet().stream()
                .map(entry -> new ReportDetailResponse.ContactMethodResponse(
                        entry.getKey(),
                        entry.getValue().stream().toList()
                ))
                .toList();

        List<ReportDetailResponse.DamageResponse> damages = reports.stream()
                .map(report -> new ReportDetailResponse.DamageResponse(
                        report.getId(),
                        maskReporterName(report.getReporter().getNickname()),
                        report.getReason(),
                        report.getCreatedAt()
                ))
                .toList();

        List<String> evidenceImageUrls = evidenceImageRepository.findAllByReportIn(reports).stream()
                .map(EvidenceImage::getImageName)
                .map(gcsUrlBuilder::buildImageUrl)
                .filter(Objects::nonNull)
                .toList();

        Report latestReport = reports.get(0);
        return new ReportDetailResponse(
                latestReport.getCompanyName(),
                (long) reports.size(),
                latestReport.getCreatedAt(),
                contactMethods,
                damages,
                evidenceImageUrls
        );
    }

    private String maskReporterName(String name) {
        if (name == null || name.isBlank()) {
            return "익명님";
        }
        if (name.length() <= 2) {
            return name.charAt(0) + "*님";
        }
        return name.substring(0, 2) + "*".repeat(Math.min(8, name.length())) + "님";
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
