package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.analysis.dto.response.AnalysisItemListResponse;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.SortType;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {

    private final AnalysisItemRepository analysisItemRepository;
    private final MemberRepository memberRepository;

    public List<AnalysisItemListResponse> getAnalysisItemList(String email, SortType sortType) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<AnalysisItem> analysisItems = analysisItemRepository.findByUserWithSummaryOrderByCreatedAtDesc(member);

        List<AnalysisItemListResponse> response = analysisItems.stream()
                .map(item -> AnalysisItemListResponse.of(
                        item,
                        item.getAnalysisSummary() != null ?
                            item.getAnalysisSummary().getRiskScore() : null
                ))
                .collect(Collectors.toList());

        if (sortType == SortType.RISK_SCORE) {
            response.sort(Comparator.comparing(AnalysisItemListResponse::riskScore, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        return response;
    }
}