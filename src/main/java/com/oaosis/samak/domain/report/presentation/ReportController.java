package com.oaosis.samak.domain.report.presentation;

import com.oaosis.samak.domain.report.application.ReportService;
import com.oaosis.samak.domain.report.dto.request.ReportCreateRequest;
import com.oaosis.samak.domain.report.dto.response.ReportCreateResponse;
import com.oaosis.samak.domain.report.dto.response.ReportListResponse;
import com.oaosis.samak.domain.report.enums.ReportSearchType;
import com.oaosis.samak.domain.report.enums.ReportSortType;
import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "신고", description = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportListResponse>>> getReportList(
            @Parameter(description = "검색 유형", example = "COMPANY_NAME")
            @RequestParam ReportSearchType searchType,
            @Parameter(description = "정렬 기준", example = "LATEST")
            @RequestParam(defaultValue = "LATEST") ReportSortType sortType,
            @Parameter(description = "검색어", example = "ABC")
            @RequestParam String keyword
    ) {
        List<ReportListResponse> responses = reportService.getReportList(searchType, sortType, keyword);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "신고 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<ReportCreateResponse>> createReport(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Parameter(description = "신고 등록 요청", required = true)
            @RequestBody ReportCreateRequest request
    ) {
        ReportCreateResponse response = reportService.createReport(user.getEmail(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}