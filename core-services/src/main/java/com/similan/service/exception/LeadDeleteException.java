package com.similan.service.exception;

public class LeadDeleteException extends RuntimeException {

	private static final long serialVersionUID = 6888871965351217053L;

	public LeadDeleteException() {
		super();
	}

	public LeadDeleteException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LeadDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public LeadDeleteException(String message) {
		super(message);
	}

	public LeadDeleteException(Throwable cause) {
		super(cause);
	}

}
