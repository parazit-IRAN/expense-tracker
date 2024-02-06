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
//        Locale locale = LocaleContextHolder.getLocale();
        Locale locale = new Locale("fa");
        return getText(msgCode, locale);
    }

    public static String getText(String msgCode, Object... params) {
//        Locale locale = LocaleContextHolder.getLocale();
        Locale locale = new Locale("fa");
        return getText(msgCode, locale, params);
    }

    static String getText(String msgCode, Locale locale, Object... params) {
        return messageSource.getMessage(msgCode, params, locale);
    }
}