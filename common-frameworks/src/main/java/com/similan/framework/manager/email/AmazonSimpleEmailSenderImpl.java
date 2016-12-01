package com.similan.framework.manager.email;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Slf4j
@Component
public class AmazonSimpleEmailSenderImpl implements EmailSender {
  @Autowired
	private AmazonSimpleEmailService sesService;

  @Override
  public String getId() {
    return "ses";
  }
  
	/**
	 * send message 
	 */
	public void send(String sender, String recipient, String replyTo,
			String subject, String bodyMsg) {
		
        Body body = new Body();
        body.setHtml( new Content( bodyMsg ) );
        
        SendEmailRequest req = new SendEmailRequest( sender,
                new Destination( Collections.singletonList( recipient ) ),
                new Message( new Content( subject ), body ) );
        
        if (replyTo != null  && replyTo.trim().length() > 0) {
            req.setReplyToAddresses( Collections.singleton( replyTo ) );
        }
        
        try {
            sesService.sendEmail( req );
        }
        catch(Exception exp){
        	//swallowing the exception since EC2 sometimes 
        	//throw unverified email address
        	log.error("Cannot send email ", exp);        	
        }
	}

}
