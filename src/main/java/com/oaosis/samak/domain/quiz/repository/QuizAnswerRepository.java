package com.oaosis.samak.domain.quiz.repository;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.quiz.entity.Quiz;
import com.oaosis.samak.domain.quiz.entity.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {

    Optional<QuizAnswer> findByQuizAndMember(Quiz quiz, Member member);
}