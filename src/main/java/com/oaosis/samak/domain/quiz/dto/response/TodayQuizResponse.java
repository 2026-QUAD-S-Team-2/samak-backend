package com.oaosis.samak.domain.quiz.dto.response;

import com.oaosis.samak.domain.quiz.entity.Quiz;
import com.oaosis.samak.domain.quiz.entity.QuizAnswer;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오늘의 퀴즈 응답")
public record TodayQuizResponse(
        @Schema(description = "퀴즈 ID", example = "1")
        Long id,
        @Schema(description = "퀴즈 문제", example = "해외 취업 시 급여를 현금으로만 지급하겠다는 회사는 안전하다.")
        String question,
        @Schema(description = "퀴즈 정답", example = "false")
        Boolean answer,
        @Schema(description = "퀴즈 해설", example = "해외 취업 시 급여를 현금으로만 지급하겠다는 회사는 안전하지 않습니다. 이는 세금 회피나 불법적인 활동의 가능성을 시사할 수 있으며, 근로자 보호 측면에서도 문제가 될 수 있습니다.")
        String explanation,
        @Schema(description = "퀴즈를 풀었는지 여부", example = "true")
        Boolean isSolved,
        @Schema(description = "정답을 맞췄는지 여부", example = "true")
        Boolean isCorrect
) {
    public static TodayQuizResponse of(Quiz quiz, QuizAnswer quizAnswer) {
        return new TodayQuizResponse(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getExplanation(),
                quizAnswer != null,
                quizAnswer != null ? quizAnswer.getIsCorrect() : null
        );
    }
}