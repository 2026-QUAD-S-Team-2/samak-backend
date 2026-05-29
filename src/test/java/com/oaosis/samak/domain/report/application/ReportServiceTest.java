package com.oaosis.samak.domain.report.application;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.domain.report.dto.response.ReportDetailResponse;
import com.oaosis.samak.domain.report.entity.EvidenceImage;
import com.oaosis.samak.domain.report.entity.Report;
import com.oaosis.samak.domain.report.entity.ReportIdentifier;
import com.oaosis.samak.domain.report.exception.ReportErrorCode;
import com.oaosis.samak.domain.report.exception.ReportException;
import com.oaosis.samak.domain.report.repository.EvidenceImageRepository;
import com.oaosis.samak.domain.report.repository.ReportIdentifierRepository;
import com.oaosis.samak.domain.report.repository.ReportRepository;
import com.oaosis.samak.global.entity.ContactType;
import com.oaosis.samak.infra.gcs.service.GcsUrlBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock ReportRepository reportRepository;
    @Mock EvidenceImageRepository evidenceImageRepository;
    @Mock ReportIdentifierRepository reportIdentifierRepository;
    @Mock MemberRepository memberRepository;
    @Mock GcsUrlBuilder gcsUrlBuilder;

    @InjectMocks ReportService reportService;

    @Test
    void getReportDetail_withCompanyName_returnsScreenData() {
        Member firstReporter = mock(Member.class);
        when(firstReporter.getNickname()).thenReturn("systemuser");
        Member secondReporter = mock(Member.class);
        when(secondReporter.getNickname()).thenReturn("김철수");

        LocalDateTime latestReportedAt = LocalDateTime.of(2026, 3, 7, 10, 0);
        LocalDateTime olderReportedAt = LocalDateTime.of(2026, 3, 2, 9, 0);

        Report latestReport = mock(Report.class);
        when(latestReport.getId()).thenReturn(2L);
        when(latestReport.getCompanyName()).thenReturn("현대간다이설");
        when(latestReport.getReason()).thenReturn("돈부터 먼저 입금하라고 하는 회사가 어디있습니까?");
        when(latestReport.getCreatedAt()).thenReturn(latestReportedAt);
        when(latestReport.getReporter()).thenReturn(secondReporter);

        Report olderReport = mock(Report.class);
        when(olderReport.getId()).thenReturn(1L);
        when(olderReport.getReason()).thenReturn("회사 명부터 구라같음. 직접 전화해봤는데 의심됨.");
        when(olderReport.getCreatedAt()).thenReturn(olderReportedAt);
        when(olderReport.getReporter()).thenReturn(firstReporter);

        ReportIdentifier kakao = mock(ReportIdentifier.class);
        when(kakao.getType()).thenReturn(ContactType.KAKAO_TALK);
        when(kakao.getValue()).thenReturn("카카오톡");
        ReportIdentifier kakaoOpenChat = mock(ReportIdentifier.class);
        when(kakaoOpenChat.getType()).thenReturn(ContactType.KAKAO_TALK);
        when(kakaoOpenChat.getValue()).thenReturn("오픈채팅");
        ReportIdentifier message = mock(ReportIdentifier.class);
        when(message.getType()).thenReturn(ContactType.MESSAGE);
        when(message.getValue()).thenReturn("메시지");

        EvidenceImage firstImage = mock(EvidenceImage.class);
        when(firstImage.getImageName()).thenReturn("evidence-1.png");
        EvidenceImage secondImage = mock(EvidenceImage.class);
        when(secondImage.getImageName()).thenReturn("evidence-2.png");

        List<Report> reports = List.of(latestReport, olderReport);
        when(reportRepository.findAllByCompanyNameWithReporterOrderByCreatedAtDesc("현대간다이설"))
                .thenReturn(reports);
        when(reportIdentifierRepository.findAllByReportIn(reports)).thenReturn(List.of(kakao, kakaoOpenChat, message));
        when(evidenceImageRepository.findAllByReportIn(reports)).thenReturn(List.of(firstImage, secondImage));
        when(gcsUrlBuilder.buildImageUrl("evidence-1.png")).thenReturn("https://cdn.test/evidence-1.png");
        when(gcsUrlBuilder.buildImageUrl("evidence-2.png")).thenReturn("https://cdn.test/evidence-2.png");

        ReportDetailResponse response = reportService.getReportDetail("현대간다이설");

        assertThat(response.companyName()).isEqualTo("현대간다이설");
        assertThat(response.reportCount()).isEqualTo(2L);
        assertThat(response.latestReportedAt()).isEqualTo(latestReportedAt);
        assertThat(response.contactMethods())
                .extracting("type", "values")
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(ContactType.KAKAO_TALK, List.of("카카오톡", "오픈채팅")),
                        org.assertj.core.groups.Tuple.tuple(ContactType.MESSAGE, List.of("메시지"))
                );
        assertThat(response.damages())
                .extracting("reportId", "reporterName", "reason", "reportedAt")
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(2L, "김철***님", "돈부터 먼저 입금하라고 하는 회사가 어디있습니까?", latestReportedAt),
                        org.assertj.core.groups.Tuple.tuple(1L, "sy********님", "회사 명부터 구라같음. 직접 전화해봤는데 의심됨.", olderReportedAt)
                );
        assertThat(response.evidenceImageUrls())
                .containsExactly("https://cdn.test/evidence-1.png", "https://cdn.test/evidence-2.png");
    }

    @Test
    void getReportDetail_withoutReports_throwsReportNotFound() {
        when(reportRepository.findAllByCompanyNameWithReporterOrderByCreatedAtDesc("없는회사"))
                .thenReturn(List.of());

        assertThatThrownBy(() -> reportService.getReportDetail("없는회사"))
                .isInstanceOf(ReportException.class)
                .satisfies(ex -> assertThat(((ReportException) ex).getErrorCode())
                        .isEqualTo(ReportErrorCode.REPORT_NOT_FOUND));
    }
}
