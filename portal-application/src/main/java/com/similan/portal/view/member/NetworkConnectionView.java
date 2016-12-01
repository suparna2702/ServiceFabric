package com.similan.portal.view.member;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;

@Scope("view")
@Component("networkConnectionView")
public class NetworkConnectionView extends BaseView {
	private static final long serialVersionUID = 1L;
	
	@Autowired(required = true)
	private MemberInfoDto memberInfo;
	
	private List<SocialActorContactDto> contactList = null;
	
	@PostConstruct
	public void init() {
		contactList = this.getNetworkService()
				.getExtendedConnections(this.getMemberKey(memberInfo));
	}

	public List<SocialActorContactDto> getContactList() {
		return contactList;
	}

}
