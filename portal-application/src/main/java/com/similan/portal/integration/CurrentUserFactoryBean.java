package com.similan.portal.integration;

import org.springframework.beans.factory.FactoryBean;

import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.portal.service.security.SecurityUtils;

/**
 * {@link FactoryBean} implementation that provides the currently
 * loggedin {@link PersistentUser}, if any is available.
 * <p>
 * The reference returned by this class can be accessed from both
 * spring beans and JSF pages (but is intended for the latter).
 * @author psaavedra
 */
public class CurrentUserFactoryBean implements FactoryBean<PersistentUser> {

	public PersistentUser getObject() throws Exception {
		return SecurityUtils.getCurrentUser();
	}

	public Class<?> getObjectType() {
		return PersistentUser.class;
	}

	public boolean isSingleton() {
		return false;
	}

}
