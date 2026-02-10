package com.oaosis.samak.infra.redis.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum RedisErrorCode implements BaseErrorCode {
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "REDIS-001", "Redis 값 타입이 올바르지 않습니다.")
    ;

    private final HttpStatus status;
    private final String value;
    private final String message;
}