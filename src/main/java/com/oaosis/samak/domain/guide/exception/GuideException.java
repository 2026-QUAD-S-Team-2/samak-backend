package com.oaosis.samak.domain.guide.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class GuideException extends BaseException {

    public GuideException(GuideErrorCode errorCode) {
        super(errorCode);
    }
}