package com.oaosis.samak.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record OAuth2LoginRequest(
        @Schema(description = "OAuth2 제공자", example = "GOOGLE")
        String provider,

        @Schema(description = "ID Token", example = "eyJhbGciOiJSUzI1NiIs")
        String idToken
) {
}