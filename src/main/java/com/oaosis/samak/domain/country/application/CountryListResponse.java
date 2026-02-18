package com.oaosis.samak.domain.country.application;

import io.swagger.v3.oas.annotations.media.Schema;

public record CountryListResponse(
        @Schema(description = "국가 코드", example = "US")
        String code,
        @Schema(description = "국가명", example = "United States")
        String name
) {
}
