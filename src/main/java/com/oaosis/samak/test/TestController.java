package com.oaosis.samak.test;

import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트", description = "개발 테스트용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final TestService testService;

    @Operation(summary = "테스트용 액세스 토큰 발급")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> fakeLogin(
            @Schema(description = "사용자 이메일", example = "samak@google.com")
            @RequestParam String email
    ) {
        TokenResponse tokenResponse = testService.login(email);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }
}