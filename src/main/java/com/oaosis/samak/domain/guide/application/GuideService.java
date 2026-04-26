package com.oaosis.samak.domain.guide.application;

import com.oaosis.samak.domain.guide.dto.response.GuideCardResponse;
import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import com.oaosis.samak.infra.gcs.service.GcsUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuideService {

    private final GuideCacheService guideCacheService;
    private final GcsUrlBuilder gcsUrlBuilder;

    public List<GuideCardResponse> getGuideCardsByCategory(GuideCategory category) {
        return guideCacheService.getGuideCardsByCategory(category).stream()
                .map(guideCard -> GuideCardResponse.of(guideCard, gcsUrlBuilder.buildImageUrl(guideCard.getImageName())))
                .toList();
    }
}