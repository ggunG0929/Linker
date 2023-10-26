package Linker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender sender;

	@Value("${spring.mail.username}")
	private String fromId;

	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		String from = "링커<" + fromId + "@gmail.com>";
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		sender.send(message);
	}
}
