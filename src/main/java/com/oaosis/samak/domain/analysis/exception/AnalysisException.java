package com.oaosis.samak.domain.analysis.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class AnalysisException extends BaseException {

    public AnalysisException(AnalysisErrorCode errorCode) {
        super(errorCode);
    }
}