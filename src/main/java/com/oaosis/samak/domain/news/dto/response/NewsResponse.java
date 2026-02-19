package com.oaosis.samak.domain.news.dto.response;

import com.oaosis.samak.domain.news.entity.News;
import io.swagger.v3.oas.annotations.media.Schema;

public record NewsResponse(
        Long id,
        @Schema(description = "뉴스 제목", example = "전국 최초 '취업사기 피해예방 조례' 통과")
        String title,
        @Schema(description = "뉴스 요약", example = "이번 조례는 허위, 과장 구인 광고와 채용을 빙자한 ...")
        String summary,
        @Schema(description = "뉴스 링크", example = "https://www.example.com/news/1")
        String link,
        @Schema(description = "뉴스 배경 이미지 URL", example = "https://www.example.com/images/news1.jpg")
        String backgroundImageUrl) {

    public static NewsResponse of(News news, String backgroundImageUrl) {
        return new NewsResponse(
                news.getId(),
                news.getTitle(),
                news.getSummary(),
                news.getLink(),
                backgroundImageUrl
        );
    }
}
