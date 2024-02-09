package ir.snapp.pay.configuration.security.jwt;


import io.jsonwebtoken.*;
import ir.snapp.pay.domain.Authority;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import ir.snapp.pay.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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

	private final UserService userService;

	public JwtTokenUtil(@Lazy UserService userService) {
		this.userService = userService;
	}

	public String generateAccessToken(String email, String password) {
		User user = userService.getUser(email, password);
		String authorities = user.getAuthorities().stream()
				.map(Authority::getName)
				.map(SimpleGrantedAuthority::new)
				.map(SimpleGrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		Map<String, Object> map = new HashMap<>();
		map.put(AUTHORITIES_KEY, authorities);
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
		return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
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

	public boolean validateToken(String authToken) throws ExpenseException {
		try {
			Jwts.parser().setSigningKey(secret_key).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace.", e);
			throw new ExpenseException(ExpenseExceptionType.TOKEN_IS_NOT_VALID_EXCEPTION);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace.", e);
			throw new ExpenseException(ExpenseExceptionType.TOKEN_EXPIRED_EXCEPTION);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace.", e);
			throw new ExpenseException(ExpenseExceptionType.TOKEN_IS_NOT_VALID_EXCEPTION);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace.", e);
			throw new ExpenseException(ExpenseExceptionType.TOKEN_IS_NOT_VALID_EXCEPTION);
		} catch (Exception e) {
			log.info("Unknown Exception.");
			log.trace("Unknown Exception.", e);
			throw new ExpenseException(ExpenseExceptionType.TOKEN_IS_NOT_VALID_EXCEPTION);
		}
	}
}