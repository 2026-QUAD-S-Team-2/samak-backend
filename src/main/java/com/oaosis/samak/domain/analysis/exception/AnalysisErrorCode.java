package com.oaosis.samak.domain.analysis.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AnalysisErrorCode implements BaseErrorCode {
    ANALYSIS_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-001", "분석 항목을 찾을 수 없습니다."),
    AI_ANALYSIS_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-002", "AI 분석 결과를 찾을 수 없습니다."),
    FORBIDDEN_ANALYSIS_ITEM(HttpStatus.FORBIDDEN, "ANALYSIS-003", "해당 분석 항목에 접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}