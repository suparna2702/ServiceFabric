package com.similan.portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;
import com.similan.service.api.SocialManagementService;

@Service("socialService")
public class SocialServiceImpl extends BaseService implements SocialService {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SocialManagementService socialManagementService;

	@Transactional
	public void inviteLinkedInInitiate(List<LinkedInConnection> invitees,
			MemberInfoDto inviter, String accessToken, String secretAccessToken) {
		socialManagementService.inviteLinkedInInitiate(invitees, inviter,
				accessToken, secretAccessToken);
	}

	@Transactional
	public MemberInfoDto mergeMemberWithLinkedInData(MemberInfoDto data,
			String accessToken, String secretAccessToken) {
		return socialManagementService.mergeMemberWithLinkedInData(data,
				accessToken, secretAccessToken);
	}

	@Transactional
	public List<LinkedInConnection> getLinkedInConnections(String accessToken,
			String secretAccessToken) {
		return socialManagementService.getLinkedInConnections(accessToken,
				secretAccessToken);
	}

}
