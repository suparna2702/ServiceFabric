package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Account {
	
	@Column
	private String userName;
	
	@Column
	private String password;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
