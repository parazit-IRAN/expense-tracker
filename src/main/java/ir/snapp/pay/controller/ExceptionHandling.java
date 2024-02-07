package ir.snapp.pay.controller;

import ir.snapp.pay.exception.ExpenseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static ir.snapp.pay.exception.ExpenseExceptionType.*;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> handleAnyException(Throwable ex, NativeWebRequest request) {
		ExpenseRestResponse expenseRestResponse = new ExpenseRestResponse<>();
		ExpenseException ee;
		if (ex instanceof InsufficientAuthenticationException) {
			ee = new ExpenseException(TOKEN_EXPIRED_EXCEPTION);
		} else if (ex instanceof AccessDeniedException) {
			ee = new ExpenseException(PERMISSION_DENIED_EXCEPTION);
		} else {
			ee = new ExpenseException(DEFAULT_EXCEPTION);
		}
		expenseRestResponse.setErrorCode(ee.getCode());
		expenseRestResponse.setErrorMessage(ee.getMessages());
		return new ResponseEntity<>(expenseRestResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}


}
