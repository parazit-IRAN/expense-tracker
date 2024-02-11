package ir.snapp.pay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.snapp.pay.configuration.security.jwt.TokenProvider;
import ir.snapp.pay.dto.LoginDto;
import ir.snapp.pay.dto.TokenOutputDto;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/authenticate")
@Tag(name = "Authenticate", description = "The Authenticate API. Its provides to get a token.")
public class AuthenticateController extends BaseController {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final TokenProvider tokenProvider;
	@Value("${app.jwt.token-validity-in-seconds}")
	private long tokenValidityInSeconds;

	public AuthenticateController(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.tokenProvider = tokenProvider;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "create a token")
	public ResponseEntity<TokenOutputDto> getToken(@Valid @RequestBody LoginDto loginDTO) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getEmail(),
				loginDTO.getPassword()
		);

		try {
			return success(createToken(authenticationToken));
		} catch (Exception e) {
			return failure(e);
		}
	}

	private TokenOutputDto createToken(UsernamePasswordAuthenticationToken authenticationToken) {
		Authentication authentication;
		try {
			authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		} catch (Exception e) {
			throw new ExpenseException(ExpenseExceptionType.USER_NOT_FOUND_EXCEPTION);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = this.tokenProvider.createToken(authentication);
		return TokenOutputDto.builder().token(token).build();
	}
}
