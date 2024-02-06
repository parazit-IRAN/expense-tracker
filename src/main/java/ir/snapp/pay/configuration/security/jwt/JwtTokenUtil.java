package ir.snapp.pay.configuration.security.jwt;


import io.jsonwebtoken.*;
import ir.snapp.pay.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenUtil {
	@Value("${app.jwt.token-validity-in-seconds}")
	private long expire_duration;
	@Value("${app.jwt.secret}")
	private String secret_key;
	private static final String AUTHORITIES_KEY = "auth";

	public String generateAccessToken(User user) {
		String authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		Map<String, Object> map = new HashMap<>();
		map.put("auth", authorities);
		map.put("sub", user.getEmail());
		return Jwts.builder()
				.setClaims(map)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expire_duration))
				.signWith(SignatureAlgorithm.HS512, secret_key)
				.compact();
	}


	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Collection<? extends GrantedAuthority> authorities = getGrantedAuthorities(claims);
		return getUsernamePasswordAuthenticationToken(token, claims, authorities);
	}

	private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token
			, Claims claims, Collection<? extends GrantedAuthority> authorities) {
		User principal = new User();
		principal.setEmail(claims.getSubject());
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secret_key)
				.parseClaimsJws(token)
				.getBody();
	}

	private static Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {
		return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secret_key).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace.", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace.", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace.", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace.", e);
		} catch (Exception e) {
			log.info("Unknown Exception.");
			log.trace("Unknown Exception.", e);
		}

		return false;
	}
}