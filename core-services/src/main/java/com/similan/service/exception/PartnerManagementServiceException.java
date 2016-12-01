package com.similan.service.exception;

public class PartnerManagementServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PartnerManagementServiceException(){
		super();
	}
	
	public PartnerManagementServiceException(String message, Throwable cause){
		super(message, cause);
	}
	
	public PartnerManagementServiceException(String message){
		super(message);
	}
	
	public PartnerManagementServiceException(Throwable cause){
		super(cause);
	}

}
