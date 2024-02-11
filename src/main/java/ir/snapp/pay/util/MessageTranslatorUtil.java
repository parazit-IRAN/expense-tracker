package ir.snapp.pay.util;

import ir.snapp.pay.domain.User;
import ir.snapp.pay.repository.UserRepository;
import liquibase.repackaged.org.apache.commons.lang3.ArrayUtils;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

@UtilityClass
public class MessageTranslatorUtil {

	private static MessageSource messageSource;
	private static UserRepository userRepository;

	public static void setUserRepository(UserRepository userRepository) {
		MessageTranslatorUtil.userRepository = userRepository;
	}

	public static void setMessageSource(MessageSource messageSource) {
		MessageTranslatorUtil.messageSource = messageSource;
	}

	public static String getText(String msgCode) {
		Locale locale = getUserLocale();
		return getText(msgCode, locale);
	}

	public Locale getUserLocale() {
		return SecurityUtils.getCurrentUserLogin()
				.map(userEmail -> getUserLocaleByEmail(userEmail)
						.orElseGet(LocaleContextHolder::getLocale))
				.orElse(LocaleContextHolder.getLocale());
	}

	private Optional<Locale> getUserLocaleByEmail(String userEmail) {
		return userRepository.findOneByEmailIgnoreCase(userEmail)
				.map(User::getLanguage)
				.map(language -> {
					try {
						return new Locale(language);
					} catch (IllegalArgumentException e) {
						return LocaleContextHolder.getLocale();
					}
				});
	}

	public static String getText(String msgCode, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		Object[] filteredParams = filterParams(params);
		return getText(msgCode, locale, filteredParams);
	}

	static String getText(String msgCode, Locale locale, Object... params) {
		return messageSource.getMessage(msgCode, params, locale);
	}

	private static Object[] filterParams(Object... params) {
		Object[] filteredParams = new Object[params.length];
		int filteredIndex = 0;
		for (Object param : params) {
			if (param instanceof Integer && (Integer) param == Integer.MAX_VALUE) {
				continue;
			}
			filteredParams[filteredIndex++] = param;
		}
		if (filteredIndex < params.length) {
			Object[] result = new Object[filteredIndex];
			System.arraycopy(filteredParams, 0, result, 0, filteredIndex);
			return result;
		}
		ArrayUtils.reverse(filteredParams);
		return filteredParams;
	}
}