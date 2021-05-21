package br.com.robertocarneiro.pautaapi.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class MessageUtil {

    private static final MessageSource MESSAGE_SOURCE = messageSource();

    private MessageUtil() {}

    private static ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public static String get(String code, Object... args) {
        return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static String get(String code) {
        return MESSAGE_SOURCE.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
