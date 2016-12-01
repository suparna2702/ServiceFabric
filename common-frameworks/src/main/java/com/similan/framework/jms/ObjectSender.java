package com.similan.framework.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

@Slf4j
public class ObjectSender<T extends Serializable> {

	private JmsTemplate jmsTemplate;

	public ObjectSender(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void send(final T object, final Integer scheduleDelay) {
		
		log.info("send message :" + object.toString()
				          + " schedule delay " + scheduleDelay);
		
		jmsTemplate.send(new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setLongProperty("JMS_JBOSS_SCHEDULED_DELIVERY", 
						       (System.currentTimeMillis()+ scheduleDelay));
				message.setObject(object);
				return message;
			}
		});
	}

	public void send(final T object) {
		log.info("send message :" + object.toString());
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setObject(object);
				return message;
			}
		});
	}
}
