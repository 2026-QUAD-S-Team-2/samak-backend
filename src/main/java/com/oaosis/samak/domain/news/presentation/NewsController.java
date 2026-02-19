package com.oaosis.samak.domain.news.presentation;

import com.oaosis.samak.domain.news.application.NewsService;
import com.oaosis.samak.domain.news.dto.response.NewsResponse;
import com.oaosis.samak.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "뉴스", description = "뉴스 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "배너 뉴스 리스트 조회", description = "메인페이지 배너 뉴스 리스트를 조회합니다.")
    @GetMapping("/banner")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getActiveNews() {
        List<NewsResponse> response = newsService.getActiveNews();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}