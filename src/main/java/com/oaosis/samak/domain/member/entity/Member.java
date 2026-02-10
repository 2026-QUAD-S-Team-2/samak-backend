package com.oaosis.samak.domain.member.entity;

import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
import com.oaosis.samak.domain.member.entity.enums.Role;
import com.oaosis.samak.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private OAuth2Provider provider;

    @Column(name = "provider_id", nullable = false, length = 100)
    private String providerId;

    @Column(nullable = false)
    private String email;

    @Column(name = "is_onboarded", nullable = false)
    private boolean isOnboarded = false;

    @Builder
    public Member(Role role, OAuth2Provider provider, String providerId, String email) {
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.isOnboarded = false;
    }
}