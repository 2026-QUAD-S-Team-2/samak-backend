package com.oaosis.samak.global.security.jwt.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class CustomJwtException extends BaseException {

	public CustomJwtException(JwtErrorCode errorCode) {
		super(errorCode);
	}
}