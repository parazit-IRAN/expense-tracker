package ir.snapp.pay.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ResourceBundleConfig {

    public static final String CLASSPATH_MESSAGES = "classpath:messages/messages";

    @Bean
    public MessageSource messageSource() {
        var rs = new ReloadableResourceBundleMessageSource();
        rs.setBasenames(CLASSPATH_MESSAGES);
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }
}