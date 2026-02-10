package com.oaosis.samak.domain.country.presentation;

import com.oaosis.samak.domain.country.application.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "국가", description = "국가 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;
}