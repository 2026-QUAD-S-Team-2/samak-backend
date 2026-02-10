package com.oaosis.samak.domain.guide.repository;

import com.oaosis.samak.domain.guide.entity.GuideCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideCardRepository extends JpaRepository<GuideCard, Long> {
}