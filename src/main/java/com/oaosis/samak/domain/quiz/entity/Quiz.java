package com.oaosis.samak.domain.quiz.entity;

import com.oaosis.samak.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false)
    private Boolean answer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "quiz_date", nullable = false, unique = true)
    private LocalDate quizDate;

    public Quiz(String question, Boolean answer, String explanation, LocalDate quizDate) {
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
        this.quizDate = quizDate;
    }
}