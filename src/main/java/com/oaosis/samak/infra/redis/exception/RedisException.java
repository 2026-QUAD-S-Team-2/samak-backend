package com.oaosis.samak.infra.redis.exception;

import com.oaosis.samak.global.exception.BaseException;

public class RedisException extends BaseException {

    public RedisException(RedisErrorCode errorCode) { super(errorCode);}
}