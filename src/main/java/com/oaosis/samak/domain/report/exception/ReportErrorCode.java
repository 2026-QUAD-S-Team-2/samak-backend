package com.oaosis.samak.domain.report.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ReportErrorCode implements BaseErrorCode {
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT-001", "신고를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}