package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.analysis.dto.request.AnalysisItemCreateRequest;
import com.oaosis.samak.domain.analysis.dto.response.AIAnalysisResultResponse;
import com.oaosis.samak.domain.analysis.dto.response.AnalysisItemDetailResponse;
import com.oaosis.samak.domain.analysis.dto.response.AnalysisItemListResponse;
import com.oaosis.samak.domain.analysis.dto.response.CountryWarningResponse;
import com.oaosis.samak.domain.analysis.entity.AIAnalysisResult;
import com.oaosis.samak.domain.analysis.entity.AnalysisImage;
import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.entity.CountryWarning;
import com.oaosis.samak.domain.analysis.enums.SortType;
import com.oaosis.samak.domain.analysis.exception.AnalysisErrorCode;
import com.oaosis.samak.domain.analysis.exception.AnalysisException;
import com.oaosis.samak.domain.analysis.repository.AIAnalysisResultRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisImageRepository;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.domain.analysis.repository.CountryWarningRepository;
import com.oaosis.samak.domain.country.entity.City;
import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.domain.country.exception.CountryErrorCode;
import com.oaosis.samak.domain.country.exception.CountryException;
import com.oaosis.samak.domain.country.repository.CityRepository;
import com.oaosis.samak.domain.country.repository.CountryRepository;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
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
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AnalysisImageRepository analysisImageRepository;
    private final AIAnalysisResultRepository aiAnalysisResultRepository;
    private final CountryWarningRepository countryWarningRepository;

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

    @Transactional
    public void createAnalysisItem(String email, AnalysisItemCreateRequest request) {
        Member member = getMember(email);
        Country country = getCountry(request);
        City city = getCity(request);

        AnalysisItem analysisItem = new AnalysisItem(
                member,
                request.contactType(),
                request.sourceUrl(),
                request.notes(),
                country,
                city,
                request.companyName(),
                request.salary()
        );

        List<AnalysisImage> images = request.imageNames().stream()
                .map(i -> new AnalysisImage(analysisItem, i))
                .toList();

        analysisItemRepository.save(analysisItem);
        analysisImageRepository.saveAll(images);

        CountryMetricsSnapshot countryMetricsSnapshot = countryMetricsSnapshotRepository.findTopByCountryOrderBySnapshotDateDesc(country)
                .orElseThrow(() -> new CountryException(CountryErrorCode.COUNTRY_METRICS_NOT_FOUND));

        //TODO: AI 분석 요청, 국가별 경고 메시지 생성 요청 로직 추가
        AIAnalysisResult aiAnalysisResult = new AIAnalysisResult(analysisItem, 75, "HIGH", "분석 결과 요약 텍스트");
        aiAnalysisResultRepository.save(aiAnalysisResult);

        String warningMessage = CountryMetricsMessageGenerator.generateMessage(country, countryMetricsSnapshot);
        CountryWarning countryWarning = new CountryWarning(analysisItem, warningMessage);

        countryWarningRepository.save(countryWarning);
    }

    private City getCity(AnalysisItemCreateRequest request) {
        return cityRepository.findById(request.cityId())
                .orElseThrow(() -> new CountryException(CountryErrorCode.CITY_NOT_FOUND));
    }

    private Country getCountry(AnalysisItemCreateRequest request) {
        return countryRepository.findByCode(request.countryCode())
                .orElseThrow(() -> new CountryException(CountryErrorCode.COUNTRY_NOT_FOUND));
    }

    public AIAnalysisResultResponse getAIAnalysisResult(String email, Long analysisItemId) {
        AnalysisItem analysisItem = getAnalysisItem(email, analysisItemId);

        AIAnalysisResult aiAnalysisResult = aiAnalysisResultRepository.findByAnalysisItem(analysisItem)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.AI_ANALYSIS_RESULT_NOT_FOUND));

        return AIAnalysisResultResponse.from(aiAnalysisResult);
    }

    private AnalysisItem getAnalysisItem(String email, Long analysisItemId) {
        return analysisItemRepository.findByMember_EmailAndId(email, analysisItemId)
                .orElseThrow(() -> new AnalysisException(AnalysisErrorCode.ANALYSIS_ITEM_NOT_FOUND));
    }
}