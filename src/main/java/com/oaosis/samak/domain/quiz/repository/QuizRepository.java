package com.oaosis.samak.domain.quiz.repository;

import com.oaosis.samak.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}