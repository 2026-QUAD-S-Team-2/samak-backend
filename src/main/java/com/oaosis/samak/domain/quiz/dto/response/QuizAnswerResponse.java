package com.oaosis.samak.domain.quiz.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuizAnswerResponse(
        @Schema(description = "정답 여부", example = "true")
        Boolean isCorrect,

        @Schema(description = "퀴즈의 정답", example = "false")
        Boolean correctAnswer,

        @Schema(description = "해설", example = "해외 취업 시 급여는 반드시 은행 계좌로 받아야 합니다.")
        String explanation
) {
}