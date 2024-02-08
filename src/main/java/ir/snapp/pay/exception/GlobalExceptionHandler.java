package ir.snapp.pay.exception;


import ir.snapp.pay.controller.ExpenseRestResponse;
import ir.snapp.pay.util.ExceptionTranslatorUtil;
import ir.snapp.pay.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ExpenseRestResponse handlePermissionDenied(AccessDeniedException ex) {
		log.error("permission denied {}", ex.getMessage(), ex);
		ExpenseRestResponse<Object> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setErrorCode(ExpenseExceptionType.PERMISSION_DENIED_EXCEPTION.getErrorCode());
		expenseRestResponse.setErrorMessage(List.of(ExpenseExceptionType.PERMISSION_DENIED_EXCEPTION.getErrorMessage()));
		return expenseRestResponse;
	}

	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExpenseRestResponse handleBindingException(BindException ex) {
		log.error("received invalid parameter {}", ex.getMessage(), ex);
		ExpenseRestResponse<Object> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setErrorCode(3001);
		expenseRestResponse.setErrorMessage(GeneralUtil.buildErrorReasonInCaseOfBindingFailure(ex.getBindingResult()));
		return expenseRestResponse;
	}

	@ExceptionHandler({PSQLException.class})
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ExpenseRestResponse handlingDbException(PSQLException ex) {
		log.error("db duplicate exception thrown : {}", ex.getMessage(), ex);
		ExpenseRestResponse<Object> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setErrorCode(3002);
		expenseRestResponse.setErrorMessage(List.of(ExceptionTranslatorUtil.findCause(ex)));
		return expenseRestResponse;
	}

	@ExceptionHandler({ExpenseException.class})
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ExpenseRestResponse handlingDbException(ExpenseException ex) {
		log.error("custom exception thrown : {}", ex.getMessage(), ex);
		ExpenseRestResponse<Object> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setErrorCode(ex.getCode());
		expenseRestResponse.setErrorMessage(ex.getMessages());
		return expenseRestResponse;
	}

}
