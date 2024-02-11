package ir.snapp.pay.provider.sms;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SecondEmailProvider implements Notification {
	@Override
	public void send(String recipient, String message) {
		System.out.println("Sending SMS By SecondEmailProvider to " + recipient + ": " + message);
	}
}
