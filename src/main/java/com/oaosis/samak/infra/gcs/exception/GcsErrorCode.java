package com.oaosis.samak.infra.gcs.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GcsErrorCode implements BaseErrorCode {
    IMAGE_FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GCS-001", "이미지 파일 업로드에 실패하였습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}
