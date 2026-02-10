package com.oaosis.samak.domain.auth.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-001", "잘못된 토큰입니다."),
	TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH-002", "토큰이 존재하지 않습니다."),
	UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH-003", "지원하지 않는 OAuth2 제공자입니다."),
	OAUTH_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-004", "OAuth2 인증에 실패했습니다.")
	;

	private final HttpStatus status;
	private final String value;
	private final String message;
}