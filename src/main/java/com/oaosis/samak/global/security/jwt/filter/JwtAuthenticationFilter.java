package com.oaosis.samak.global.security.jwt.filter;

import com.oaosis.samak.global.config.properties.SecurityProperties;
import com.oaosis.samak.global.security.jwt.JwtTokenAuthenticator;
import com.oaosis.samak.global.security.jwt.JwtTokenProvider;
import com.oaosis.samak.global.security.jwt.exception.CustomJwtException;
import com.oaosis.samak.global.security.jwt.exception.JwtErrorCode;
import com.oaosis.samak.infra.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtTokenAuthenticator jwtTokenAuthenticator;
	private final RedisService redisService;
	private final SecurityProperties securityProperties;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return isWhitelisted(requestURI);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String token = jwtTokenProvider.extractTokenFromHeader(request);

		if (token != null) {
			// Redis 블랙리스트에 있는 토큰인지 확인
			if (redisService.hasKey("blacklist:" + token)) {
				throw new CustomJwtException(JwtErrorCode.TOKEN_REVOKED);
			}

			Claims claims = jwtTokenProvider.validateToken(token);
			Authentication authentication = jwtTokenAuthenticator.getAuthentication(claims);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private boolean isWhitelisted(String requestURI) {
		return securityProperties.getWhitelist().stream()
				.anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
	}
}