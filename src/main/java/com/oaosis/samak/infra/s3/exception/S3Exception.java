package com.oaosis.samak.infra.s3.exception;


import com.oaosis.samak.global.exception.BaseException;

public class S3Exception extends BaseException {

    public S3Exception(S3ErrorCode errorCode) { super(errorCode);}
}