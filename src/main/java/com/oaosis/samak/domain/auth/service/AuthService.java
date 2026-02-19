package com.oaosis.samak.domain.auth.service;

import com.oaosis.samak.global.security.oauth2.dto.OAuth2UserInfo;
import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
import com.oaosis.samak.domain.auth.exception.AuthErrorCode;
import com.oaosis.samak.domain.auth.exception.CustomAuthException;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.entity.enums.Role;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.global.security.dto.TokenResponse;
import com.oaosis.samak.global.security.jwt.JwtTokenProvider;
import com.oaosis.samak.global.security.oauth2.service.OAuth2AuthenticationService;
import com.oaosis.samak.infra.redis.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationService oauth2AuthenticationService;
    private final MemberRepository memberRepository;

    public TokenResponse loginWithOAuth2(OAuth2Provider provider, String idToken) {
        OAuth2UserInfo userInfo = oauth2AuthenticationService.verifyIdToken(provider, idToken);

        Member member = memberRepository.findByProviderId(userInfo.getProviderId())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .role(Role.MEMBER)
                            .provider(provider)
                            .providerId(userInfo.getProviderId())
                            .email(userInfo.getEmail())
                            .nickname(userInfo.getName())
                            .build();
                    return memberRepository.save(newMember);
                });

        return jwtTokenProvider.issueAccessToken(member.getEmail(), member.getId(), member.getRole(), member.isOnboarded());
    }

    public void logout(String accessToken) {
        if (accessToken == null) {
            throw new CustomAuthException(AuthErrorCode.TOKEN_NOT_FOUND);
        }

        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        if (expiration > 0) {
            redisService.saveValue("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        }
    }
}