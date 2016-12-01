package com.similan.portal.integration;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.service.MemberService;
import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.service.exception.CoreServiceException;

/**
 * An {@link AbstractAuthenticationProcessingFilter} subclass that
 * validates and populates the referring organization info.
 * @author psaavedra
 */
@Slf4j
public class EmbeddedAuthenticationProcessingFilter extends
		AbstractAuthenticationProcessingFilter {


	@Autowired
	private MemberService memberService;
	
	public EmbeddedAuthenticationProcessingFilter(
			String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		String memberId = request.getParameter("mid");
		log.info("Attempting embedded authentication for member {}", memberId);
		if (StringUtils.isBlank(memberId)) {
			throw new UsernameNotFoundException("Could not find referring organization");
		}
		try {
			MemberInfoDto member = this.memberService.embeddedLogin(memberId);
			PersistentUser user = new PersistentUser(member.getId(),
					member.getUserName(), member.getEmployer().getId(),
					Arrays.asList(new SimpleGrantedAuthority("ROLE_EMBEDDED")));
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user,
	                null, user.getAuthorities());
	        result.setDetails(this.authenticationDetailsSource.buildDetails(request));
	        return result;
		} catch (CoreServiceException e) {
			log.error("Could not perform embedded login for member " + memberId, e);
			throw new AuthenticationServiceException("Error while attempting embedded login");
		}
	}

}
