package com.oaosis.samak.domain.company.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class CompanyException extends BaseException {

    public CompanyException(CompanyErrorCode errorCode) {
        super(errorCode);
    }
}