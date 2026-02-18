package com.oaosis.samak.domain.country.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CountryErrorCode implements BaseErrorCode {

    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "COUNTRY-001", "국가를 찾을 수 없습니다."),
    CITY_NOT_FOUND(HttpStatus.NOT_FOUND, "COUNTRY-002", "도시를 찾을 수 없습니다."),
    COUNTRY_METRICS_NOT_FOUND(HttpStatus.NOT_FOUND, "COUNTRY-003", "국가 지표를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}