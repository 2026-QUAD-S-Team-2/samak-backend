package com.oaosis.samak.domain.member.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class MemberException extends BaseException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}