package com.oaosis.samak.domain.quiz.application;

import com.oaosis.samak.domain.quiz.repository.QuizAnswerRepository;
import com.oaosis.samak.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {
}