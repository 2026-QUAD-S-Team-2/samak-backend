package com.oaosis.samak.domain.quiz.presentation;

import com.oaosis.samak.domain.quiz.application.QuizService;
import com.oaosis.samak.domain.quiz.dto.response.TodayQuizResponse;
import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "퀴즈", description = "퀴즈 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "오늘의 퀴즈 및 사용자 정답 조회")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<TodayQuizResponse>> getTodayQuiz(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        TodayQuizResponse response = quizService.getTodayQuiz(user.getEmail());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}