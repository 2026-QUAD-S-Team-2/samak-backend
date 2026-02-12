package com.oaosis.samak.domain.guide.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GuideErrorCode implements BaseErrorCode {
    GUIDE_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "GUIDE-001", "가이드 카드를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}