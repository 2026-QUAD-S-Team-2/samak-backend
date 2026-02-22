package com.oaosis.samak.domain.analysis.entity;

import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.global.entity.BaseTimeEntity;
import com.oaosis.samak.domain.country.entity.City;
import com.oaosis.samak.domain.country.entity.Country;
import com.oaosis.samak.global.entity.ContactType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "analysisItem", fetch = FetchType.LAZY)
    private AIAnalysisResult AIAnalysisResult;

    @OneToOne(mappedBy = "analysisItem", fetch = FetchType.LAZY)
    private CountryWarning countryWarning;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private AnalysisItemType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type")
    ContactType contactType;

    @Column(name = "source_url", columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status = AnalysisStatus.PENDING;

    public AnalysisItem(Member member, ContactType contactType, String sourceUrl, String notes, Country country, City city, String companyName, BigDecimal salary) {
        this.member = member;
        this.contactType = contactType;
        this.sourceUrl = sourceUrl;
        this.notes = notes;
        this.country = country;
        this.city = city;
        this.companyName = companyName;
        this.salary = salary;
        this.status = AnalysisStatus.PENDING;
    }

    public void updateStatus(AnalysisStatus status) {
        this.status = status;
    }
}