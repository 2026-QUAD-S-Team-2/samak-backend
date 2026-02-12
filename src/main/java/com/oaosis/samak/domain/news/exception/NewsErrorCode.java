package com.oaosis.samak.domain.news.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum NewsErrorCode implements BaseErrorCode {
    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "NEWS-001", "뉴스를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}