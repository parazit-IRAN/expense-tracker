package ir.snapp.pay.exception;


import lombok.Data;

import java.util.function.Supplier;

@Data
public class ExpenseException extends RuntimeException implements Supplier<ExpenseException> {
	private int code;
	private String message;

	@Override
	public ExpenseException get() {
		return this;
	}

	public ExpenseException(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public ExpenseException(ExpenseExceptionType expenseExceptionType){
		this(expenseExceptionType.getErrorCode(), expenseExceptionType.getErrorMessage());
	}
}
