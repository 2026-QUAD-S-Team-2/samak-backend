package com.oaosis.samak.domain.auth.presentation;

import com.oaosis.samak.domain.auth.dto.OAuth2LoginRequest;
import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
import com.oaosis.samak.domain.auth.service.AuthService;
import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.dto.TokenResponse;
import com.oaosis.samak.global.security.jwt.resolver.AccessToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "인증", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "OAuth2 소셜 로그인")
    @PostMapping("/oauth2/login")
    public ResponseEntity<ApiResponse<TokenResponse>> oauth2Login(
            @RequestBody OAuth2LoginRequest request) {
        OAuth2Provider provider = OAuth2Provider.valueOf(request.provider());
        TokenResponse tokenResponse = authService.loginWithOAuth2(provider, request.idToken());
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(hidden = true)
            @AccessToken String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}