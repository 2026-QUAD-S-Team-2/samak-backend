package com.oaosis.samak.infra.openFeign.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oaosis.samak.domain.country.entity.Country;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                if (salary == null) {
                        return new AIAnalyzeRequest(country.getCode(), false, imageUrls, null);
                }

                BigDecimal hoursPerYear = BigDecimal.valueOf(8 * 5 * 52);
                BigDecimal hourlyWage = salary.divide(hoursPerYear, 2, RoundingMode.HALF_UP);

                String salaryText = country.getCurrencyCode() + " "
                        + hourlyWage.stripTrailingZeros().toPlainString() + " hour";

                return new AIAnalyzeRequest(country.getCode(), false, imageUrls, salaryText);
        }
}