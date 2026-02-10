package com.oaosis.samak.domain.analysis.presentation;

import com.oaosis.samak.domain.analysis.application.AnalysisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "분석", description = "분석 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analysis")
public class AnalysisController {
}