package com.oaosis.samak.infra.rabbitMQ.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oaosis.samak.domain.country.entity.Country;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public record AnalysisRequest(
        Long analysisItemId,

        @JsonProperty("countryCode")
        String countryCode,

        @JsonProperty("debug")
        boolean debug,

        @JsonProperty("imageUrls")
        List<String> imageUrls,

        @JsonProperty("salaryText")
        String salaryText
) {
        public static AnalysisRequest of(Long analysisItemId, Country country, BigDecimal salary, List<String> imageUrls) {
                if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
                        return new AnalysisRequest(analysisItemId, country.getCode(), false, imageUrls, null);
                }

                BigDecimal hoursPerYear = BigDecimal.valueOf(8 * 5 * 52);
                BigDecimal hourlyWage = salary.divide(hoursPerYear, 2, RoundingMode.HALF_UP);

                String salaryText = country.getCurrencyCode() + " "
                        + hourlyWage.stripTrailingZeros().toPlainString() + " hour";

                return new AnalysisRequest(analysisItemId, country.getCode(), false, imageUrls, salaryText);
        }
}