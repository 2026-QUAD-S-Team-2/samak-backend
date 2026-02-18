package com.oaosis.samak.domain.country.presentation;

import com.oaosis.samak.domain.country.application.CityListResponse;
import com.oaosis.samak.domain.country.application.CountryListResponse;
import com.oaosis.samak.domain.country.service.CountryService;
import com.oaosis.samak.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "국가", description = "국가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @Operation(summary = "국가 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CountryListResponse>>> getCountries() {
        return ResponseEntity.ok(ApiResponse.success(countryService.getCountries()));
    }

    @Operation(summary = "국가별 도시 리스트 조회")
    @GetMapping("/{countryCode}/cities")
    public ResponseEntity<ApiResponse<List<CityListResponse>>> getCities(
            @Schema(description = "국가 코드", example = "US")
            @PathVariable String countryCode) {
        return ResponseEntity.ok(ApiResponse.success(countryService.getCities(countryCode)));
    }
}