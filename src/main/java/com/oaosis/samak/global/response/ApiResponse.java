package com.oaosis.samak.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oaosis.samak.global.exception.BaseErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통 응답
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(example = "200")
    private String code;
    @Schema(example = "API 요청 성공")
    private String message;
    private T data;

    private static String SUCCESS_MESSAGE = "API 요청 성공";

    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .data(null)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .data(data)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public static <T> ApiResponse<T> failure(BaseErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .code(errorCode.getValue())
                .message(errorCode.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> failure(String code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}