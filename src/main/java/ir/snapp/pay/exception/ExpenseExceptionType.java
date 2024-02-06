package ir.snapp.pay.exception;


import ir.snapp.pay.util.MessageTranslatorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ExpenseExceptionType {
	DEFAULT_EXCEPTION(1, "exception.in.expense.api");

	private int errorCode;
	private String errorMessage;

	public String getErrorMessage() {
		return MessageTranslatorUtil.getText(errorMessage);
	}
}
