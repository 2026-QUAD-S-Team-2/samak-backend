package com.oaosis.samak.global.security.jwt;

import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenAuthenticator {

    private final JwtTokenProvider jwtTokenProvider;

    public Authentication getAuthentication(Claims claims) {
        AuthenticatedUser user = new AuthenticatedUser(
                jwtTokenProvider.getEmail(claims),
                jwtTokenProvider.getId(claims),
                jwtTokenProvider.getRole(claims)
        );

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());
    }
}