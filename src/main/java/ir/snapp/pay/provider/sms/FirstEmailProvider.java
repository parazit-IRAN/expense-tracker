package ir.snapp.pay.provider.sms;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Primary
public class FirstEmailProvider implements Notification {
	@Override
	public void send(String recipient, String message) {
		try {
			Thread.sleep(5000); // 5 second delay
		} catch (InterruptedException e) {
		}
		System.err.println("Sending SMS By FirstEmailProvider to " + recipient + ": " + message);
	}
}
