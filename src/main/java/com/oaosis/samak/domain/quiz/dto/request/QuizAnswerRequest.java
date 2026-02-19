package com.oaosis.samak.domain.quiz.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuizAnswerRequest(
        @Schema(description = "퀴즈 ID", example = "1")
        Long quizId,

        @Schema(description = "사용자가 입력한 답변", example = "false")
        Boolean answer
) {
}