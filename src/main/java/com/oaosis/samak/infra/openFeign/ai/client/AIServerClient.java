package com.oaosis.samak.infra.openFeign.ai.client;

import com.oaosis.samak.infra.openFeign.ai.dto.request.AIAnalyzeRequest;
import com.oaosis.samak.infra.openFeign.ai.dto.response.AIAnalyzeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ai-client",
        url = "${feign.ai.url}"
)
public interface AIServerClient {

    @PostMapping(value = "/v1/analyze/image", consumes = "application/json", produces = "application/json")
    AIAnalyzeResponse analyzeImage(@RequestBody AIAnalyzeRequest request);
}