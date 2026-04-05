package com.oaosis.samak.domain.guide.repository;

import com.oaosis.samak.domain.guide.entity.GuideCard;
import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideCardRepository extends JpaRepository<GuideCard, Long> {

    List<GuideCard> findAllByCategory(GuideCategory category);
}