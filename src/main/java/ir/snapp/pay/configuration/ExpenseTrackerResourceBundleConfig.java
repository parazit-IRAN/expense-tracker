package ir.snapp.pay.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.PostConstruct;

@Configuration
public class ExpenseTrackerResourceBundleConfig {

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @PostConstruct
    public void addMessageBaseName() {
        messageSource.addBasenames("classpath:messages/messages");
    }
}