package com.similan.framework.manager.email;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BasicEmailSender implements EmailSender {
  @Autowired
	private JavaMailSender mailSender;
  
	@Override
	public String getId() {
	  return "basic";
	}

	public void send(String sender, String recipient, String replyTo,
			String subject, String bodyMsg) {
		
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(recipient);
		msg.setFrom(sender);
		msg.setText(bodyMsg);
        try {
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
        	log.error("Cannot send mail ", ex);
        }
	}

}
