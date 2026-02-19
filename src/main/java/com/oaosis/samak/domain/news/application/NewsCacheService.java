package com.oaosis.samak.domain.news.application;

import com.oaosis.samak.domain.news.entity.News;
import com.oaosis.samak.domain.news.repository.NewsRepository;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsCacheService {

    private final NewsRepository newsRepository;

    @Cacheable(value = "news", key = "'all'")
    public List<News> getActiveNews() {
        log.info("Cache miss - Loading news from database");
        return newsRepository.findAllByIsActiveTrue();
    }
}