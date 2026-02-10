package com.oaosis.samak.domain.auth.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class CustomAuthException extends BaseException {

	public CustomAuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}
}