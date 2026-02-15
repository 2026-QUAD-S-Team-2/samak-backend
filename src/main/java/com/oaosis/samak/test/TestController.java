package com.oaosis.samak.test;

import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final TestService testService;

    @Operation(summary = "테스트용 액세스 토큰 발급(email: test@gmail.com)")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> fakeLogin() {
        TokenResponse tokenResponse = testService.login();
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }
}