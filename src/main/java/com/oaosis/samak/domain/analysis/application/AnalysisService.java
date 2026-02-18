package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.analysis.dto.response.AnalysisItemDetailResponse;
import com.oaosis.samak.domain.analysis.dto.response.AnalysisItemListResponse;
import com.oaosis.samak.domain.analysis.dto.response.CountryWarningResponse;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.entity.AnalysisSummary;
import com.oaosis.samak.domain.analysis.entity.CountryWarning;
import com.oaosis.samak.domain.analysis.enums.SortType;
import com.oaosis.samak.domain.analysis.exception.AnalysisErrorCode;
import com.oaosis.samak.domain.analysis.exception.AnalysisException;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemIdentifierRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.domain.report.repository.ReportIdentifierRepository;
import com.oaosis.samak.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {

    private final AnalysisItemRepository analysisItemRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final AnalysisItemIdentifierRepository analysisItemIdentifierRepository;
    private final ReportIdentifierRepository reportIdentifierRepository;

    public List<AnalysisItemListResponse> getAnalysisItemList(String email, SortType sortType) {
        Member member = getMember(email);

        List<AnalysisItem> analysisItems = analysisItemRepository.findByUserWithSummaryOrderByCreatedAtDesc(member);

        List<AnalysisItemListResponse> response = analysisItems.stream()
                .map(item -> AnalysisItemListResponse.of(
                        item,
                        item.getAIAnalysisResult() != null ?
                            item.getAIAnalysisResult().getRiskScore() : null
                ))
                .collect(Collectors.toList());

        if (sortType == SortType.RISK_SCORE) {
            response.sort(Comparator.comparing(AnalysisItemListResponse::score, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        return response;
    }

    public AnalysisItemDetailResponse getAnalysisItemDetail(String email, Long analysisItemId) {
        AnalysisItem analysisItem = getAnalysisItem(email, analysisItemId);

        return AnalysisItemDetailResponse.of(analysisItem);
    }

    public CountryWarningResponse getCountryWarning(String email, Long analysisItemId) {
        AnalysisItem analysisItem = getAnalysisItem(email, analysisItemId);

        CountryWarning countryWarning = analysisItem.getCountryWarning();
        if (countryWarning == null) {
            return new CountryWarningResponse(null);
        }

        return new CountryWarningResponse(countryWarning.getWarningMessage());
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private AnalysisItem getAnalysisItem(Long analysisItemId) {
        return analysisItemRepository.findByIdWithDetails(analysisItemId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));
    }

    private void validateOwnership(Member member, AnalysisItem analysisItem) {
        if (!analysisItem.getMember().getId().equals(member.getId())) {
            throw new AnalysisException(AnalysisErrorCode.FORBIDDEN_ANALYSIS_ITEM);
        }
    }

    private AnalysisItemDetailResponse.AnalysisSummaryDto mapToSummaryDto(AnalysisSummary analysisSummary) {
        if (analysisSummary == null) {
            return null;
        }
        return new AnalysisItemDetailResponse.AnalysisSummaryDto(
                analysisSummary.getScore(),
                analysisSummary.getMessage()
        );
    }
}