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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "provider_id", nullable = false, length = 20)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider provider;

    @Column(name = "profile_image_name")
    private String profileImageName;

    private boolean isOnboarded = false;

    @Builder
    public Member(String email, String nickname, Role role, String providerId, OAuth2Provider provider, String profileImageName) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.providerId = providerId;
        this.provider = provider;
        this.profileImageName = profileImageName;
        this.isOnboarded = false;
    }
}