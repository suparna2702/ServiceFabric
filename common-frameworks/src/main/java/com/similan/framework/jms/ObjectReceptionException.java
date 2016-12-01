package com.similan.framework.jms;

import javax.jms.JMSException;

public class ObjectReceptionException extends RuntimeException {

	private static final long serialVersionUID = -3473673404786451966L;

	public ObjectReceptionException(JMSException cause) {
		super(cause);
	}

}
