package com.oaosis.samak.domain.guide.presentation;

import com.oaosis.samak.domain.guide.application.GuideService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가이드", description = "가이드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class GuideController {

    private final GuideService guideService;
}