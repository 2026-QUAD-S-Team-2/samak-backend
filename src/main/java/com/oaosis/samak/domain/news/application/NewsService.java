package com.oaosis.samak.domain.news.application;

import com.oaosis.samak.domain.news.dto.response.NewsResponse;
import com.oaosis.samak.infra.gcs.service.GcsUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsCacheService newsCacheService;
    private final GcsUrlBuilder gcsUrlBuilder;

    public List<NewsResponse> getActiveNews() {
        return newsCacheService.getActiveNews().stream()
                .map(news -> {
                    String backgroundImageUrl = news.getBackgroundImageName() != null
                            ? gcsUrlBuilder.buildImageUrl(news.getBackgroundImageName())
                            : null;
                    return NewsResponse.of(news, backgroundImageUrl);
                })
                .toList();
    }
}