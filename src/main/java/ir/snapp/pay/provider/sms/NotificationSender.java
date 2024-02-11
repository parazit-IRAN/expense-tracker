package ir.snapp.pay.provider.sms;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationSender {

	private final Notification notification;

	public void sendNotification(String recipient, String message) {
		notification.send(recipient, message);
		log.debug("Send sms by irancell recipient :{}, message:{}, ", recipient, message);
	}
}
