package com.similan.portal.service;

import java.util.List;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;

public interface SocialService {

	/**
	 * @param invitees
	 * @param inviter
	 * @param accessToken
	 * @param secretAccessToken
	 */
	
	public void inviteLinkedInInitiate(List<LinkedInConnection> invitees,
			MemberInfoDto inviter, String accessToken, String secretAccessToken);

	/**
	 * @param data
	 * @param accessToken
	 */
	public MemberInfoDto mergeMemberWithLinkedInData(MemberInfoDto data,
			String accessToken, String secretAccessToken);

	/**
	 * @param accessToken
	 * @return
	 */
	public List<LinkedInConnection> getLinkedInConnections(String accessToken,
			String secretAccessToken);

}
