package com.oaosis.samak.domain.analysis.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AnalysisErrorCode implements BaseErrorCode {
    ANALYSIS_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-001", "분석 항목을 찾을 수 없습니다."),
    ANALYSIS_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-002", "분석 요약을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}