package com.oaosis.samak.domain.quiz.application;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.domain.quiz.dto.request.QuizAnswerRequest;
import com.oaosis.samak.domain.quiz.dto.response.QuizAnswerResponse;
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

    private final QuizAnswerRepository quizAnswerRepository;
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final QuizCacheService quizCacheService;

    public TodayQuizResponse getTodayQuiz(String email) {
        Quiz quiz = getTodayQuiz();
        Member member = getMember(email);
        QuizAnswer quizAnswer = quizAnswerRepository.findByQuizAndMember(quiz, member)
                .orElse(null);

        return TodayQuizResponse.of(quiz, quizAnswer);
    }

    @Transactional
    public QuizAnswerResponse submitQuizAnswer(String email, QuizAnswerRequest request) {
        Member member = getMember(email);
        Quiz quiz = getTodayQuiz();

        quizAnswerRepository.findByQuizAndMember(quiz, member)
                .ifPresent(existing -> {
                    throw new QuizException(QuizErrorCode.QUIZ_ALREADY_ANSWERED);
                });

        boolean isCorrect = request.answer().equals(quiz.getAnswer());

        QuizAnswer quizAnswer = new QuizAnswer(quiz, member, request.answer(), isCorrect);
        quizAnswerRepository.save(quizAnswer);

        return new QuizAnswerResponse(isCorrect, quiz.getAnswer(), quiz.getExplanation());
    }

    private Quiz getTodayQuiz() {
        LocalDate today = LocalDate.now();
        return quizCacheService.getTodayQuizFromCache(today);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}