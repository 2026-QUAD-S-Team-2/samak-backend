package com.oaosis.samak.domain.quiz.entity;

import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean answer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    public QuizAnswer(Quiz quiz, Member member, Boolean answer, Boolean isCorrect) {
        this.quiz = quiz;
        this.member = member;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }
}