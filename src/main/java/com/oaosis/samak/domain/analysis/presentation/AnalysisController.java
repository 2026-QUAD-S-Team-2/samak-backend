package com.oaosis.samak.domain.analysis.presentation;

import com.oaosis.samak.domain.analysis.application.AnalysisService;
import com.oaosis.samak.domain.analysis.dto.response.AnalysisItemListResponse;
import com.oaosis.samak.domain.analysis.enums.SortType;
import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "분석", description = "분석 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @Operation(summary = "분석 아이템 목록 조회")
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<List<AnalysisItemListResponse>>> getAnalysisItemList(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Parameter(description = "정렬 방식", example = "LATEST")
            @RequestParam(defaultValue = "LATEST") SortType sortType
    ) {
        List<AnalysisItemListResponse> responses = analysisService.getAnalysisItemList(user.getEmail(), sortType);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}