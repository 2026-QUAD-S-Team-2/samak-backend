package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.domain.country.entity.CountryMetricsSnapshot;
import com.oaosis.samak.domain.country.entity.WageUnit;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

public class CountryMetricsMessageGenerator {

    public static String generateMessage(
            Country country,
            CountryMetricsSnapshot snapshot
    ) {

        if (snapshot == null) {
            return null;
        }

        Locale locale = new Locale(
                country.getLanguageCode(),
                country.getCode()
        );

        String currencyCode = country.getCurrencyCode();

        StringBuilder message = new StringBuilder();

        boolean hasAvg = snapshot.getAvgIncome() != null;
        boolean hasMedian = snapshot.getMedianIncome() != null;
        boolean hasMinWage = snapshot.getMinWage() != null
                && snapshot.getMinWageUnit() != null;

        //평균/중위 연봉
        if (hasAvg || hasMedian) {

            message.append(country.getName()).append("의 ");

            if (hasAvg) {
                message.append("평균 연봉은 ")
                        .append(formatCurrency(snapshot.getAvgIncome(), currencyCode, locale));
            }

            if (hasMedian) {
                if (hasAvg) message.append("이며 ");
                message.append("중위 연봉은 ")
                        .append(formatCurrency(snapshot.getMedianIncome(), currencyCode, locale));
            }

            message.append("입니다.\n");
        }

        //최저임금
        if (hasMinWage) {
            message.append("법정 최저임금은 ")
                    .append(convertUnit(snapshot.getMinWageUnit()))
                    .append(" 기준 ")
                    .append(formatCurrency(snapshot.getMinWage(), currencyCode, locale))
                    .append("입니다.\n");
        }

        //기준일
        message.append("해당 정보는 ")
                .append(snapshot.getSnapshotDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd", locale)))
                .append(" 기준 통계입니다.");

        return message.toString();
    }

    private static String formatCurrency(
            BigDecimal amount,
            String currencyCode,
            Locale locale
    ) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setCurrency(Currency.getInstance(currencyCode));
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount);
    }

    private static String convertUnit(WageUnit unit) {
        return switch (unit) {
            case YEARLY -> "연간";
            case MONTHLY -> "월";
            case WEEKLY -> "주";
            case DAILY -> "일";
            case HOURLY -> "시간";
        };
    }
}