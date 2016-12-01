package com.similan.portal.integration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.service.OrganizationService;
import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.portal.service.security.SecurityUtils;
import com.similan.service.api.MemberManagementService;

/**
 * {@link FactoryBean} implementation that provides the {@link OrganizationDetailInfoDto}
 * for the currently logged in user.
 * @author psaavedra
 */
@Slf4j
public class OrganizationInfoFactoryBean implements FactoryBean<OrganizationDetailInfoDto> {


	@Autowired
	private MemberManagementService managementService;

	@Autowired
	private OrganizationService organizationService;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Transactional
	public OrganizationDetailInfoDto getObject() throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("Obtained current authentication object {}", authentication);
		PersistentUser user = SecurityUtils.fromAuthentication(authentication);
		
		if (user == null) {
			return null;
		}
		log.info("Currently logged in user is {}", user.getUsername());
		
		try {
			
			/*if(user.getOrgInfo() == null){
				MemberInfo member = this.managementService.memberById(user.getId());
				OrganizationAutoCompleteTag employer = member.getEmployer();
				if (employer != null) {
					OrganizationInfo orgInfo = this.organizationService.getOrgInfoFromOrgTag(employer);
					user.setOrgInfo(orgInfo);
				}
			}*/
			
			MemberInfoDto member = this.managementService.memberById(user.getId());
			OrganizationBasicInfoDto employer = member.getEmployer();
			if (employer != null) {
				OrganizationDetailInfoDto orgInfo = this.organizationService.getOrgInfoFromOrgTag(employer);
				return orgInfo;
			}
			
			return null;
			
		} catch (Exception e) {
			log.error("Could not find a corresponding member for id "
					+ user.getId(), e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType() {
		return OrganizationDetailInfoDto.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return false;
	}

}
