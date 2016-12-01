package com.similan.domain.entity.event;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column
	private LoginModeType loginMode;
	
	@Column(length=5000)
	private String webSessionId;
	
	@Column
	private Date logoutTime;
	
	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public LoginModeType getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(LoginModeType loginMode) {
		this.loginMode = loginMode;
	}

	public String getWebSessionId() {
		return webSessionId;
	}

	public void setWebSessionId(String webSessionId) {
		this.webSessionId = webSessionId;
	}
	

}
