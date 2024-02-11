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
	TOKEN_IS_NOT_VALID_EXCEPTION(7, "token.is.not.valid"),
	AUTHORITIES_NOT_FOUND_EXCEPTION(8, "authorities.not.found"),
	USER_IS_NOT_ACTIVE_EXCEPTION(9, "user.is.not.active"),
	ACCOUNT_NOT_FOUND_EXCEPTION(10, "account.not.found"),
	CATEGORY_NOT_FOUND_EXCEPTION(11, "category.not.found"),
	TRANSACTION_NOT_FOUND_EXCEPTION(12, "transaction.not.found"),
	TRANSACTION_IS_NOT_VALID_EXCEPTION(13, "transaction.is.not.valid"),
	ACCOUNT_TYPE_NOT_FOUND_EXCEPTION(14, "accountType.not.found"),
	BUDGET_NOT_FOUND_EXCEPTION(15, "budget.not.found");

	private int errorCode;
	private String errorMessage;

	public String getErrorMessage() {
		return MessageTranslatorUtil.getText(errorMessage);
	}

	public String getErrorMessage(String val, String val2) {
		return MessageTranslatorUtil.getText(errorMessage, val, val2);
	}
}
