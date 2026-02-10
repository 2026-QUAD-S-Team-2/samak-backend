package com.oaosis.samak.domain.quiz.presentation;

import com.oaosis.samak.domain.quiz.application.QuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "퀴즈", description = "퀴즈 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;
}