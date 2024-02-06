package ir.snapp.pay.exception;


import ir.snapp.pay.util.MessageTranslatorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Getter
@ToString
@AllArgsConstructor
public enum ExpenseExceptionType {
	DEFAULT_EXCEPTION(1, "Exception In Expense API");

	private int errorCode;
	private String errorMessage;

	public String getErrorMessage() {
		Locale locale = LocaleContextHolder.getLocale();
		return MessageTranslatorUtil.getText(errorMessage, locale);
	}
}
