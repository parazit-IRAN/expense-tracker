package ir.snapp.pay.configuration.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.snapp.pay.controller.ExpenseRestResponse;
import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class JWTFilter extends GenericFilterBean {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	private final TokenProvider tokenProvider;
	private final ObjectMapper objectMapper;


	public JWTFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String jwt = resolveToken(httpServletRequest);
		if (StringUtils.hasText(jwt)) {


			try {
				this.tokenProvider.validateToken(jwt);
			} catch (ExpenseException e) {
				fillResponseWithCustomResult(servletResponse, e.getCode(), e.getMessages());
				return;
			}


			Authentication authentication = this.tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public void fillResponseWithCustomResult(ServletResponse response, int code, List<String> messages) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
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
