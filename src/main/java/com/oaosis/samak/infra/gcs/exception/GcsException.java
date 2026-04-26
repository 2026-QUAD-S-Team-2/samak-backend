package com.oaosis.samak.infra.gcs.exception;

import com.oaosis.samak.global.exception.BaseException;

public class GcsException extends BaseException {
    public GcsException(GcsErrorCode errorCode) {
        super(errorCode);
    }
}
