package com.oaosis.samak.domain.quiz.presentation;

import com.oaosis.samak.domain.quiz.application.QuizService;
import com.oaosis.samak.domain.quiz.dto.request.QuizAnswerRequest;
import com.oaosis.samak.domain.quiz.dto.response.QuizAnswerResponse;
import com.oaosis.samak.domain.quiz.dto.response.TodayQuizResponse;
import com.oaosis.samak.global.response.ApiResponse;
import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "퀴즈 답변 제출", description = "사용자가 입력한 퀴즈 답변을 저장하고 정답 여부를 반환합니다.")
    @PostMapping("/answer")
    public ResponseEntity<ApiResponse<QuizAnswerResponse>> submitQuizAnswer(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody QuizAnswerRequest request
    ) {
        QuizAnswerResponse response = quizService.submitQuizAnswer(user.getEmail(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}