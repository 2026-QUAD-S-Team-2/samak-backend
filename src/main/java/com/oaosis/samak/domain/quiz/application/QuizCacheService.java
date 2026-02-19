package com.oaosis.samak.domain.quiz.application;

import com.oaosis.samak.domain.quiz.entity.Quiz;
import com.oaosis.samak.domain.quiz.exception.QuizErrorCode;
import com.oaosis.samak.domain.quiz.exception.QuizException;
import com.oaosis.samak.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizCacheService {

    private final QuizRepository quizRepository;

    @Cacheable(value = "quizzes", key = "#date.toString()")
    public Quiz getTodayQuizFromCache(LocalDate date) {
        log.info("Cache miss - Loading quiz from database for date: {}", date);
        return quizRepository.findByQuizDate(date)
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));
    }
}