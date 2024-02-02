package ir.snapp.pay.configuration;


import ir.snapp.pay.util.MessageTranslatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslatorUtilConfigure {

	@Autowired
	public void initial(MessageSource messageSource) {
		MessageTranslatorUtil.setMessageSource(messageSource);
	}
}