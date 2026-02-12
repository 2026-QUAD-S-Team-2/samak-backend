package com.oaosis.samak.domain.report.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class ReportException extends BaseException {

    public ReportException(ReportErrorCode errorCode) {
        super(errorCode);
    }
}