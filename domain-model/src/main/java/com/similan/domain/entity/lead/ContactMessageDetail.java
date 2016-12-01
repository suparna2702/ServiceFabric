package com.similan.domain.entity.lead;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ContactMessageDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column
	private String email;
	
	@Column
	private String phone;
    
	@Column
	private String subject;
	
	@Column
	private String message;
	
	@Enumerated(EnumType.STRING)
	@Column
	private ContactMessageMode contactMode;
	
	@Enumerated(EnumType.STRING)
	@Column
	private ContactMessageType contactMessageType;
	
	public ContactMessageType getContactMessageType() {
		return contactMessageType;
	}

	public void setContactMessageType(ContactMessageType contactMessageType) {
		this.contactMessageType = contactMessageType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ContactMessageMode getContactMode() {
		return contactMode;
	}

	public void setContactMode(ContactMessageMode contactMode) {
		this.contactMode = contactMode;
	}

	@Override
	public String toString() {
		return "ContactMessageDetail [email=" + email + ", phone=" + phone
				+ ", subject=" + subject + ", message=" + message
				+ ", contactMode=" + contactMode + ", contactMessageType="
				+ contactMessageType + "]";
	}
	
	


}
