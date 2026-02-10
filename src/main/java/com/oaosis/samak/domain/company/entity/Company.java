package com.oaosis.samak.domain.company.entity;

import com.oaosis.samak.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;
}