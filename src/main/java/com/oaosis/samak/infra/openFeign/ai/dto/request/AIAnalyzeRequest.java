package com.oaosis.samak.infra.openFeign.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oaosis.samak.domain.country.entity.Country;

import java.math.BigDecimal;
import java.util.List;

public record AIAnalyzeRequest(
        @JsonProperty("countryCode")
        String countryCode,

        @JsonProperty("debug")
        boolean debug,

        @JsonProperty("imageUrls")
        List<String> imageUrls,

        @JsonProperty("salaryText")
        String salaryText
) {
        public static AIAnalyzeRequest of(Country country, BigDecimal salary, List<String> imageUrls) {
                String salaryText = salary != null ? country.getCurrencyCode() + salary : null;
                return new AIAnalyzeRequest(country.getCode(), false, imageUrls, salaryText);
        }
}