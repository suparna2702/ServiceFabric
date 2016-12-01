package com.similan.framework.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectReceiver<T extends Serializable> implements MessageListener {

	private ObjectConsumer<T> consumer;
	private Class<T> objectClass;

	public ObjectReceiver(Class<T> objectClass, ObjectConsumer<T> consumer) {
		this.objectClass = objectClass;
		this.consumer = consumer;
	}

	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		Serializable object;
		
		try {
			object = objectMessage.getObject();
			log.info("Message received from JMS " + object.toString());
		} 
		catch (JMSException e) {
			
			log.error("Exception in receiving message ", e);
			throw new ObjectReceptionException(e);
		}
		
		log.info("Object of type to be casted " + objectClass.getName());
		T castedObject = objectClass.cast(object);
		consumer.consume(castedObject);
	}

}
