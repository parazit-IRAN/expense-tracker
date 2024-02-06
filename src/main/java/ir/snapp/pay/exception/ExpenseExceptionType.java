package ir.snapp.pay.exception;


import ir.snapp.pay.util.MessageTranslatorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ExpenseExceptionType {
	DEFAULT_EXCEPTION(1, "exception.in.expense.api"),
	USER_NOT_FOUND_EXCEPTION(2, "user.not.found"),
	PASSWORD_IS_NOT_CORRECT_EXCEPTION(3, "password.is.not.correct"),
	USER_EXISTS_EXCEPTION(4, "user.exists");

	private int errorCode;
	private String errorMessage;

	public String getErrorMessage() {
		return MessageTranslatorUtil.getText(errorMessage);
	}
}
