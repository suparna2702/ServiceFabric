package com.similan.framework.dto.mail;

import java.io.Serializable;
import java.util.Date;

public class MailDto implements Serializable {

  	private static final long serialVersionUID = 1L;

	private String from;
    
    private String subject;
    
    private String body;
    
    private  Date date;
    
    public MailDto() {}

    public MailDto(String from, String subject, 
    		       String body, Date date) {
    	
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}