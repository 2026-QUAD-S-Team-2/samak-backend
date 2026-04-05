package com.oaosis.samak.domain.guide.presentation;

import com.oaosis.samak.domain.guide.application.GuideService;
import com.oaosis.samak.domain.guide.dto.response.GuideCardResponse;
import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import com.oaosis.samak.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "가이드", description = "가이드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class GuideController {

    private final GuideService guideService;

    @Operation(summary = "사기 가이드 카드 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<GuideCardResponse>>> getGuideCards(
            @Parameter(description = "가이드 카테고리 (PREVENTION: 예방, RESPONSE: 대응)", required = true)
            @RequestParam GuideCategory category) {
        List<GuideCardResponse> response = guideService.getGuideCardsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}