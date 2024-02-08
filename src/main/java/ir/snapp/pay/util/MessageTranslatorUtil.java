package ir.snapp.pay.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@UtilityClass
public class MessageTranslatorUtil {

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource) {
        MessageTranslatorUtil.messageSource = messageSource;
    }

    public static String getText(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return getText(msgCode, locale);
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
        return filteredParams;
    }
}