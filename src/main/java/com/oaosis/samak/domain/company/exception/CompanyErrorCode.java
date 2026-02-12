package com.oaosis.samak.domain.company.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CompanyErrorCode implements BaseErrorCode {
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY-001", "회사를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}