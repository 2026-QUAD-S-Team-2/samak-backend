package com.oaosis.samak.domain.report.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "신고", description = "신고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

}