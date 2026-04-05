package com.oaosis.samak.domain.guide.application;

import com.oaosis.samak.domain.guide.entity.GuideCard;
import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import com.oaosis.samak.domain.guide.repository.GuideCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuideCacheService {

    private final GuideCardRepository guideCardRepository;

    @Cacheable(value = "guides", key = "#category.name()")
    public List<GuideCard> getGuideCardsByCategory(GuideCategory category) {
        log.info("Cache miss - Loading guide cards from database. category={}", category);
        return guideCardRepository.findAllByCategory(category);
    }
}
