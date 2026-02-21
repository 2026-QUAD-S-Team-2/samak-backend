package com.oaosis.samak.domain.analysis.application;

import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.domain.country.entity.CountryMetricsSnapshot;
import com.oaosis.samak.domain.country.entity.WageUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CountryMetricsMessageGenerator {

    public static String generateMessage(
            Country country,
            CountryMetricsSnapshot snapshot,
            BigDecimal userAnnualIncome
    ) {

        if (snapshot == null) return null;
        

        String currencyCode = country.getCurrencyCode();
        String countryCode = country.getCode();

        Currency currency = Currency.getInstance(currencyCode);
        String symbol = currency.getSymbol(new Locale("", countryCode));

        StringBuilder message = new StringBuilder();

        message.append(country.getName())
                .append("의 평균 연봉은 ")
                .append(formatCurrency(snapshot.getAvgIncome(), symbol))
                .append("이며, 법정 최저임금은 ")
                .append(convertUnit(snapshot.getMinWageUnit()))
                .append(" 기준 ")
                .append(formatCurrency(snapshot.getMinWage(), symbol))
                .append("입니다.\n\n");

        BigDecimal avgRatio = userAnnualIncome
                .divide(snapshot.getAvgIncome(), 4, RoundingMode.HALF_UP);

        message.append("귀하의 연봉은 ")
                .append(formatCurrency(userAnnualIncome, symbol))
                .append("으로 이는 평균 대비 ")
                .append(avgRatio.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString())
                .append("% 수준입니다.\n\n");

        BigDecimal annualMinWage = convertToAnnual(
                snapshot.getMinWage(),
                snapshot.getMinWageUnit()
        );

        BigDecimal minRatio = userAnnualIncome
                .divide(annualMinWage, 4, RoundingMode.HALF_UP);

        // 위험 구간 분기
        if (minRatio.compareTo(BigDecimal.ONE) < 0) {

            message.append("제안된 보상은 ")
                    .append(country.getName())
                    .append("의 법정 최저임금 연환산 금액보다 낮습니다.\n")
                    .append("노동 기준에 부합하지 않을 가능성이 높으므로 급여 조건을 반드시 재확인하시기 바랍니다.");

        } else if (minRatio.compareTo(BigDecimal.valueOf(4)) >= 0) {

            message.append("제안된 보상은 ")
                    .append(country.getName())
                    .append(" 법정 최저임금의 ")
                    .append(minRatio.setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString())
                    .append("배 수준입니다.\n")
                    .append("일반적인 산업 평균을 크게 초과하는 비현실적 조건으로, ")
                    .append("허위·과장 채용 공고에서 흔히 나타나는 패턴일 수 있습니다.\n")
                    .append("기업 정보 및 계약 조건을 추가 검증하시기 바랍니다.");

        } else if (minRatio.compareTo(BigDecimal.valueOf(2)) >= 0) {

            message.append("제안된 보상은 법정 최저임금 대비 ")
                    .append(minRatio.setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString())
                    .append("배 수준으로 높은 편에 속합니다.\n")
                    .append("업무 내용 대비 합리적 수준인지 추가 검토가 필요합니다.");

        } else {

            message.append("해당 연봉은 법정 최저임금 대비 ")
                    .append(minRatio.multiply(BigDecimal.valueOf(100))
                            .setScale(1, RoundingMode.HALF_UP)
                            .stripTrailingZeros()
                            .toPlainString())
                    .append("% 수준으로 법적 기준에는 부합합니다.\n")
                    .append("근로 조건과 복리후생을 함께 검토하시기 바랍니다.");
        }

        return message.toString();
    }

    private static BigDecimal convertToAnnual(
            BigDecimal wage,
            WageUnit unit
    ) {
        final BigDecimal HOURS_PER_DAY = BigDecimal.valueOf(8);
        final BigDecimal DAYS_PER_WEEK = BigDecimal.valueOf(5);
        final BigDecimal WEEKS_PER_YEAR = BigDecimal.valueOf(52);
        final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);

        return switch (unit) {
            case HOURLY ->
                    wage.multiply(HOURS_PER_DAY)
                            .multiply(DAYS_PER_WEEK)
                            .multiply(WEEKS_PER_YEAR);
            case DAILY ->
                    wage.multiply(DAYS_PER_WEEK)
                            .multiply(WEEKS_PER_YEAR);
            case WEEKLY ->
                    wage.multiply(WEEKS_PER_YEAR);
            case MONTHLY ->
                    wage.multiply(MONTHS_PER_YEAR);
            case YEARLY -> wage;
        };
    }

    private static String formatCurrency(
            BigDecimal amount,
            String symbol
    ) {
        // 불필요한 뒤의 0 제거
        BigDecimal strippedAmount = amount.stripTrailingZeros();

        // 숫자 포맷팅 (천 단위 구분)
        NumberFormat numberFormatter = NumberFormat.getInstance(Locale.US);

        // 소수점 자릿수 결정
        int scale = Math.max(strippedAmount.scale(), 0);
        numberFormatter.setMinimumFractionDigits(0);
        numberFormatter.setMaximumFractionDigits(scale);
        numberFormatter.setGroupingUsed(true);

        return symbol + numberFormatter.format(strippedAmount);
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