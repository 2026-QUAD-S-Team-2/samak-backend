package com.oaosis.samak.global.security.jwt.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {

	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-001", "토큰이 만료되었습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-002", "잘못된 토큰입니다."),
	AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "JWT-003", "사용자 인증이 필요합니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "JWT-004", "해당 요청에 대한 접근 권한이 없습니다."),
	TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "JWT-005", "이미 무효화된 토큰입니다."),
	JWT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JWT-006", "JWT 에러가 발생하였습니다."),
	;

	private final HttpStatus status;
	private final String value;
	private final String message;
}