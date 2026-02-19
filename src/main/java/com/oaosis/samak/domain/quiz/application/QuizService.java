package com.oaosis.samak.domain.quiz.application;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.domain.quiz.dto.response.TodayQuizResponse;
import com.oaosis.samak.domain.quiz.entity.Quiz;
import com.oaosis.samak.domain.quiz.entity.QuizAnswer;
import com.oaosis.samak.domain.quiz.exception.QuizErrorCode;
import com.oaosis.samak.domain.quiz.exception.QuizException;
import com.oaosis.samak.domain.quiz.repository.QuizAnswerRepository;
import com.oaosis.samak.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final MemberRepository memberRepository;

    public TodayQuizResponse getTodayQuiz(String email) {
        LocalDate today = LocalDate.now();
        Quiz quiz = quizRepository.findByQuizDate(today)
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        QuizAnswer quizAnswer = quizAnswerRepository.findByQuizAndMember(quiz, member)
                .orElse(null);

        return TodayQuizResponse.of(quiz, quizAnswer);
    }
}