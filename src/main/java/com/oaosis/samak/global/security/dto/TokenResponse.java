package com.oaosis.samak.global.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(example = "oaosis@gmail.com")
        String email,
        @Schema(example = "1")
        Long id,
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJsaW1jeTQyM0")
        String token,
        @Schema(example = "true")
        boolean isOnboarded) {
}