package com.oaosis.samak.domain.analysis.dto.request;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.global.entity.ContactType;

import java.util.List;

public record AnalysisItemCreateRequest(
    List<String> imageNames,
    String companyName,
    String countryCode,
    Long cityId,
    ContactType contactType,
    String sourceUrl,
    String notes,
    Long salary
) {
}