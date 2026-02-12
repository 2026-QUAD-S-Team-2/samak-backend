package com.oaosis.samak.domain.quiz.exception;

import com.oaosis.samak.global.exception.BaseException;
import lombok.Getter;

@Getter
public class QuizException extends BaseException {

    public QuizException(QuizErrorCode errorCode) {
        super(errorCode);
    }
}