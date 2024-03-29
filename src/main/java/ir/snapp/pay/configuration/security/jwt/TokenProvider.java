package ir.snapp.pay.configuration.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ir.snapp.pay.dto.TokenOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
	private static final String AUTHORITIES_KEY = "auth";
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private Key key;
	private JwtParser jwtParser;
	@Value("${app.jwt.secret}")
	private String secret;
	@Value("${app.jwt.token-validity-in-seconds}")
	private long tokenValidityInMilliseconds;

	public TokenProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}


	@PostConstruct
	public void initialize() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
	}


	public TokenOutputDto createToken(UsernamePasswordAuthenticationToken authenticationToken) {
		Authentication authentication;
		try {
			authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		} catch (Exception e) {
			throw new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = this.createToken(authentication);
		return TokenOutputDto.builder().token(token).build();
	}

	private String createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);

		return Jwts
				.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = jwtParser.parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.filter(auth -> !auth.trim().isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String authToken) throws ExpenseException {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
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
