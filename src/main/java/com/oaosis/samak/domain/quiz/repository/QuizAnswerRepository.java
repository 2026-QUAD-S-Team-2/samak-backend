package com.oaosis.samak.domain.quiz.repository;

import com.oaosis.samak.domain.quiz.entity.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
}