package com.oaosis.samak.domain.country.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class CountryException extends BaseException {

    public CountryException(CountryErrorCode errorCode) {
        super(errorCode);
    }
}