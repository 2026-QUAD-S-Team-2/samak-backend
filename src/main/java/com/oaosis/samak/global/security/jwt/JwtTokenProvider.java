package com.oaosis.samak.global.security.jwt;

import com.oaosis.samak.domain.member.entity.enums.Role;
import com.oaosis.samak.global.security.dto.TokenResponse;
import com.oaosis.samak.global.security.dto.TokenType;
import com.oaosis.samak.global.security.jwt.exception.CustomJwtException;
import com.oaosis.samak.global.security.jwt.exception.JwtErrorCode;
import com.oaosis.samak.global.security.jwt.util.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	private SecretKey secretKey;

	@PostConstruct
	public void initializeSecretKey() {
		byte[] decoded = Decoders.BASE64.decode(jwtProperties.getSecretKey());
		this.secretKey = Keys.hmacShaKeyFor(decoded);
	}

	public String extractTokenFromHeader(HttpServletRequest request) {
		String header = request.getHeader(JwtProperties.AUTHORIZATION_HEADER);
		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			return null;
		}
		return header.split(" ")[1];
	}

	public TokenResponse issueAccessToken(String email, Long id, Role role, boolean isOnboarded) {
		Instant issuedAt = Instant.now();
		Instant expiration = issuedAt.plusMillis(jwtProperties.getAccessExpirationTime());
		String token = buildToken(email, id, role, TokenType.ACCESS, issuedAt, expiration);

		return new TokenResponse(email, id, token, isOnboarded);
	}

	public String buildToken(String email, Long id, Role role, TokenType tokenType, Instant issuedAt, Instant expiration) {
		return Jwts.builder()
			.setSubject(String.valueOf(id))
			.claim("email", email)
			.claim("role", role)
			.claim("tokenType", tokenType)
			.setIssuedAt(Date.from(issuedAt))
			.setExpiration(Date.from(expiration))
			.signWith(secretKey)
			.compact();
	}

	//JWT 토큰 검증
	public Claims validateToken(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(secretKey).build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
//			log.warn("[!] Expired Token: {}", e.getMessage());
			throw new CustomJwtException(JwtErrorCode.EXPIRED_TOKEN);
		} catch (MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
//			log.warn("[!] Invalid Token: {}", e.getMessage());
			throw new CustomJwtException(JwtErrorCode.INVALID_TOKEN);
		} catch (Exception e) {
//			log.error("[!] Unexpected JWT Error: {}", e.getMessage());
			throw new CustomJwtException(JwtErrorCode.JWT_ERROR);
		}
	}

	public String getSubject(Claims claims) { // 현재 subject -> id
		return claims.getSubject();
	}

	public Long getId(Claims claims) {
		return Long.parseLong(claims.getSubject());
	}

	public String getEmail(Claims claims) {
		return claims.get("email", String.class);
	}

	public Role getRole(Claims claims) {
		String role = claims.get("role", String.class);
		return Role.valueOf(role);
	}

	// 토큰의 남은 유효시간 반환 (밀리초)
	public Long getExpiration(String token) {
		Claims claims = validateToken(token);
		Date now = new Date();
		return claims.getExpiration().getTime() - now.getTime();
	}
}