package com.oaosis.samak.test;

import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
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

@Transactional
@RequiredArgsConstructor
@Service
public class TestService {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationService oauth2AuthenticationService;
    private final MemberRepository memberRepository;

    public TokenResponse login() {
        String testEmail = "test@gmail.com";

        Member member = memberRepository.findByEmail(testEmail)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .role(Role.MEMBER)
                            .provider(OAuth2Provider.GOOGLE)
                            .providerId("test")
                            .email(testEmail)
                            .build();
                    return memberRepository.save(newMember);
                });

        return jwtTokenProvider.issueAccessToken(testEmail, member.getId(), Role.MEMBER, false);
    }
}