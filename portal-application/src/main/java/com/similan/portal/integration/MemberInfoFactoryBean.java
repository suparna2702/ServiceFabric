package com.similan.portal.integration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.portal.service.security.SecurityUtils;
import com.similan.service.api.MemberManagementService;

/**
 * Spring {@link FactoryBean} implementation that generates a
 * member info object from the currently logged in user.
 * @author psaavedra
 */
@Slf4j
public class MemberInfoFactoryBean implements FactoryBean<MemberInfoDto> {


	@Autowired
	private MemberManagementService managementService;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Transactional
	public MemberInfoDto getObject() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("Obtained current authentication object {}", authentication);
		PersistentUser user = SecurityUtils.fromAuthentication(authentication);
		
		if (user == null) {
			return null;
		}
		
		log.info("Currently logged in user is {}", user.getUsername());
		try {
			
			log.info("logged in member is null fetching member from DB " + user.getUsername());
			MemberInfoDto member = this.managementService.memberById(user.getId());
			// For backward compatibility.
			member.setLogged(true);
			return member;
		} catch (Exception e) {
			log.error("Could not find a corresponding member for id "
					+ user.getId(), e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<MemberInfoDto> getObjectType() {
		return MemberInfoDto.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return false;
	}

}
