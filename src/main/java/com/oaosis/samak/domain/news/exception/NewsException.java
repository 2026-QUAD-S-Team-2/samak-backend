package com.oaosis.samak.domain.news.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NewsException extends BaseException {

    public NewsException(NewsErrorCode errorCode) {
        super(errorCode);
    }
}