package com.oaosis.samak.domain.guide.application;

import com.oaosis.samak.domain.guide.dto.response.GuideCardResponse;
import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import com.oaosis.samak.infra.s3.service.S3UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuideService {

    private final GuideCacheService guideCacheService;
    private final S3UrlBuilder s3UrlBuilder;

    public List<GuideCardResponse> getGuideCardsByCategory(GuideCategory category) {
        return guideCacheService.getGuideCardsByCategory(category).stream()
                .map(guideCard -> GuideCardResponse.of(guideCard, s3UrlBuilder.buildImageUrl(guideCard.getImageName())))
                .toList();
    }
}