package com.similan.service.exception;

public class MemberManagementServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MemberManagementServiceException(){
		super();
	}
	
	public MemberManagementServiceException(String message, Throwable cause){
		super(message, cause);
	}
	
	public MemberManagementServiceException(String message){
		super(message);
	}
	
	public MemberManagementServiceException(Throwable cause){
		super(cause);
	}


}
