package com.similan.domain.entity.event;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity(name = "MemberLoginEvent")
@DiscriminatorValue("MemberLoginEvent")
public class MemberLoginEvent extends CommunityEvent {
	
	@Embedded
	private LoginInfo loginInfo;

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
}
