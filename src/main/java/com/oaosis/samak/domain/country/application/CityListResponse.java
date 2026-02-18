package com.oaosis.samak.domain.country.application;

import io.swagger.v3.oas.annotations.media.Schema;

public record CityListResponse(
        Long id,
        @Schema(description = "도시명", example = "Chicago")
        String name
) {
}
