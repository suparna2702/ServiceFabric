package com.similan.portal.view;

import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.portal.service.security.SecurityUtils;

/**
 * A listener that listens to inactivity events from the UI and invalidates the
 * session.
 *
 * @author psaavedra
 */
@Component("idleMonitorController")
@Scope("request")
@Slf4j
public class IdleListener {


	public void idleListener() {
		PersistentUser user = SecurityUtils.getCurrentUser();
		log.warn("Session for user {} has expired due to inactivity", user);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		SecurityContextHolder.clearContext();
	}
}
