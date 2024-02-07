package ir.snapp.pay.exception;


import lombok.Data;

import java.util.List;
import java.util.function.Supplier;

@Data
public class ExpenseException extends RuntimeException implements Supplier<ExpenseException> {
	private int code;
	private List<String> messages;

	@Override
	public ExpenseException get() {
		return this;
	}

	public ExpenseException() {
	}
	public ExpenseException(int code, List<String> messages) {
		this.code = code;
		this.messages = messages;
	}
	public ExpenseException(ExpenseExceptionType expenseExceptionType){
		this(expenseExceptionType.getErrorCode(), List.of(expenseExceptionType.getErrorMessage()));
	}
}
