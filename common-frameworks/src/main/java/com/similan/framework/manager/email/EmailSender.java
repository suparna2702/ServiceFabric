package com.similan.framework.manager.email;

public interface EmailSender {
  public String getId();
  
	public void send(String sender, String recipient, 
	                 String replyTo, String subject, 
	                 String bodyMsg);
}
