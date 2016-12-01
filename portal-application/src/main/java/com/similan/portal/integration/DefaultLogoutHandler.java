package com.similan.portal.integration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.service.MemberService;
import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.portal.service.security.SecurityUtils;

/**
 * Default implementation of {@link LogoutSuccessHandler}, that sends the
 * logout event to the member service.
 * @author psaavedra
 */
@Slf4j
public class DefaultLogoutHandler implements LogoutHandler {

	@Autowired
	private MemberService memberService;


	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		PersistentUser user = SecurityUtils.fromAuthentication(authentication);
		if (user == null) {
			log.warn("Attempted to logout a user, but it's null");
			return;
		}
		try {
			MemberInfoDto member = memberService.getMemberById(user.getId());
			log.info("Loging out member {}", member);
			memberService.logout(member, request.getSession(false).getId());
		} catch (Exception e) {
			log.warn("Error while attemping to logout member " + user.getId() , e);
		}
	}

	
}
