package com.similan.service.exception;

public class CoreServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	private Exception innerException;
	
	public CoreServiceException(String message){
		super(message);
		errorCode = message;
	}
	
	public CoreServiceException(String message, Throwable cause){
		super(message, cause);
	}
	
	public CoreServiceException(String errCode, Exception innerExp){
		super(innerExp);
		this.errorCode = errCode;
		this.innerException = innerExp;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Exception getInnerException() {
		return innerException;
	}

}
