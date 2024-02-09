package ir.snapp.pay.controller;

import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.util.MessageTranslatorUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ir.snapp.pay.exception.ExpenseExceptionType.*;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

	private static final String WELL_FORMED_EMAIL = "well-formed email";
	private static final String SIZE = "size";
	private static final String IS_NULL = "null";


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


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatus status,
																  WebRequest request) {
		ExpenseRestResponse expenseRestResponse = new ExpenseRestResponse();
		expenseRestResponse.setErrorCode(0);
		List<String> messages = new ArrayList<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			if (error != null) {
				String defaultMessage = Objects.requireNonNullElse(error.getDefaultMessage(), "");
				if (defaultMessage.contains(IS_NULL)) {
					messages.add(MessageTranslatorUtil.getText(error.getField()) + " " + MUST_NOT_BE_NULL_EXCEPTION.getErrorMessage());
				} else {
					if (defaultMessage.contains(SIZE)) {
						if (error.getArguments() != null) {
							String val1 = String.valueOf(Optional.ofNullable(error.getArguments()[2]).orElse("0"));
							String val2 = String.valueOf(Optional.ofNullable(error.getArguments()[1]).orElse("0"));
							messages.add(MessageTranslatorUtil.getText(error.getField()) + " " + SIZE_IS_NOT_VALID_EXCEPTION.getErrorMessage(val1, val2));
						}
					} else {
						if (defaultMessage.contains(WELL_FORMED_EMAIL)) {
							messages.add(EMAIL_PATTERN_IS_NOT_VALID_EXCEPTION.getErrorMessage());
						}
					}
				}
			}
		}
		expenseRestResponse.setErrorMessage(messages);
		return new ResponseEntity<>(expenseRestResponse, HttpStatus.BAD_REQUEST);
	}
}
