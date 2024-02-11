package ir.snapp.pay.provider.sms;

import org.springframework.scheduling.annotation.Async;

public interface Notification {
	@Async
	void send(String recipient, String message);
}
