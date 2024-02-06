package ir.snapp.pay.controller;


import ir.snapp.pay.exception.ExpenseException;
import ir.snapp.pay.exception.ExpenseExceptionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController<T> {
	protected static final Logger logger = LogManager.getLogger(BaseController.class);

	protected ResponseEntity<ExpenseRestResponse> failure(Exception e) {
		ExpenseRestResponse expenseRestResponse = new ExpenseRestResponse<>();
		ExpenseException pe;
		if (e instanceof ExpenseException) {
			pe = (ExpenseException) e;
			logger.error(pe);
		} else {
			e.printStackTrace();
			logger.error(e.getStackTrace());
			pe = new ExpenseException(ExpenseExceptionType.DEFAULT_EXCEPTION);
		}
		expenseRestResponse.setErrorCode(pe.getCode());
		expenseRestResponse.setErrorMessage(List.of(pe.getMessage()));
		return new ResponseEntity<>(expenseRestResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	protected ResponseEntity<ExpenseRestResponse<T>> success(T result) {
		ExpenseRestResponse<T> expenseRestResponse = new ExpenseRestResponse<>();
		expenseRestResponse.setData(result);
		return new ResponseEntity(result, HttpStatus.OK);
	}
}
