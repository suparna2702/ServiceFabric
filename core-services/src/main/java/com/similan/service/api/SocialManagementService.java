package com.similan.service.api;

import java.util.List;

import org.springframework.social.linkedin.api.impl.LinkedInTemplate;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;

public interface SocialManagementService {

	/**
	 * @param invitee
	 * @param inviter
	 */
	public void inviteLinkedInInitiate(List<LinkedInConnection> invitees,
			MemberInfoDto inviter, String accessToken, String secretAccessToken);

	/**
	 * @param data
	 * @param accessToken
	 * @return
	 */
	public MemberInfoDto mergeMemberWithLinkedInData(MemberInfoDto data,
			String accessToken, String secretAccessToken);

	/**
	 * @param accessToken
	 * @return
	 */
	public List<LinkedInConnection> getLinkedInConnections(String accessToken,
			String secretAccessToken);

	/**
	 * @param token
	 * @param secretToken
	 * @return
	 */
	public LinkedInTemplate getLinkedInTemplate(String token, String secretToken);
}
