package com.similan.framework.dto.admin;

import java.io.Serializable;

public class AdminAccountDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private boolean logged;
	
	private String loggedInName;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String confirmPassword;
	
	public AdminAccountDto(){
		this.logged = false;
		this.id = Long.MIN_VALUE;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getLoggedInName() {
		return loggedInName;
	}

	public void setLoggedInName(String loggedInName) {
		this.loggedInName = loggedInName;
	}

	public boolean getLogged(){
		return logged;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}
}
