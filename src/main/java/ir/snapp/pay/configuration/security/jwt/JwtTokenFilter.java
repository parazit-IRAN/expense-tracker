package ir.snapp.pay.configuration.security.jwt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.snapp.pay.controller.ExpenseRestResponse;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private static final String AUTHORIZATION = "Authorization";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!hasAuthorizationBearer(request) &&
				SecurityContextHolder.getContext().getAuthentication() == null) {
			filterChain.doFilter(request, response);
			return;
		} else if (!hasAuthorizationBearer(request) &&
				SecurityContextHolder.getContext().getAuthentication() == null) {
			fillResponseWithCustomResult(response, ExpenseExceptionType.TOKEN_IS_NOT_VALID_EXCEPTION);
			return;
		}

		String token = getAccessToken(request);

		try {
			jwtUtil.validateToken(token);
		} catch (ExpenseException e) {
			fillResponseWithCustomResult(response, e.getCode(), e.getMessages());
			return;
		}

		setAuthenticationContext(token);
		filterChain.doFilter(request, response);
	}

	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		String header = request.getHeader(AUTHORIZATION);
		return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
	}

	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader(AUTHORIZATION);
		return header.split(" ")[1].trim();
	}

	private void setAuthenticationContext(String token) {
		Authentication authentication = jwtUtil.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public void fillResponseWithCustomResult(HttpServletResponse response, ExpenseExceptionType expenseExceptionType) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ExpenseRestResponse<ExpenseExceptionType> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setErrorCode(expenseExceptionType.getErrorCode());
		expenseRestResponse.setErrorMessage(List.of(expenseExceptionType.getErrorMessage()));
		try {
			response.getWriter().write(createErrorAsString(expenseRestResponse));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void fillResponseWithCustomResult(HttpServletResponse response, int code, List<String> messages) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ExpenseRestResponse<ExpenseExceptionType> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setErrorCode(code);
		expenseRestResponse.setErrorMessage(messages);
		try {
			response.getWriter().write(createErrorAsString(expenseRestResponse));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public String createErrorAsString(ExpenseRestResponse expenseRestResponse) {
		try {
			return new String(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(expenseRestResponse), StandardCharsets.UTF_8);
		} catch (JsonProcessingException e) {
			logger.error("could not create json object", e);
			throw new RuntimeException();
		}
	}

}

