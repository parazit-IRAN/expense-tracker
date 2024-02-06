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
	USER_EXISTS_EXCEPTION(4, "user.exists"),
	TOKEN_EXPIRED_EXCEPTION(5, "token.expired"),
	PERMISSION_DENIED_EXCEPTION(6, "permission.denied"),
	MUST_NOT_BE_NULL_EXCEPTION(7, "must.not.be.null"),
	SIZE_IS_NOT_VALID_EXCEPTION(8, "size.must.be.between.val1.to.val2"),
	EMAIL_PATTERN_IS_NOT_VALID_EXCEPTION(9, "email.pattern.is.not.valid");

	private int errorCode;
	private String errorMessage;

	public String getErrorMessage() {
		return MessageTranslatorUtil.getText(errorMessage);
	}
	public String getErrorMessage(String val, String val2) {
		return MessageTranslatorUtil.getText(errorMessage, val, val2);
	}
}
