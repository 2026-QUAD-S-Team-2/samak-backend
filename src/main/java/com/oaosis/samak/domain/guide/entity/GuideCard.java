package com.oaosis.samak.domain.guide.entity;

import com.oaosis.samak.domain.guide.entity.enums.GuideCategory;
import com.oaosis.samak.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuideCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_card_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuideCategory category;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;
}