package com.oaosis.samak.domain.quiz.exception;

import com.oaosis.samak.global.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum QuizErrorCode implements BaseErrorCode {
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "QUIZ-001", "퀴즈를 찾을 수 없습니다."),
    QUIZ_ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "QUIZ-002", "퀴즈 답변을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String value;
    private final String message;
}