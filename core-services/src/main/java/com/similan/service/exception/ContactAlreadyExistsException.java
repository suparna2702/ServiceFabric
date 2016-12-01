package com.similan.service.exception;

public class ContactAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 6888871965351217053L;

	public ContactAlreadyExistsException() {
		super();
	}

	public ContactAlreadyExistsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ContactAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContactAlreadyExistsException(String message) {
		super(message);
	}

	public ContactAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
