package com.similan.portal.service.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.MemberInviteDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.service.api.MemberManagementService;

/**
 * Spring application listener implementation that listens for {@link MemberInviteEvent}
 * and use them to trigger an invite process. 
 * @author pablo
 */
@Component
@Slf4j
public class MemberInviteListener implements ApplicationListener<MemberInviteEvent>, BeanFactoryAware {

	/**
	 * Bean factory used to obtain the member management service.
	 * Note, the lookup is done because auto wiring threw a circular dependency error.
	 */
	private BeanFactory beanFactory;

	/**
	 * The logger.
	 */

	/**
	 * Receives a member invite event and sends it to the member management
	 * service.
	 * @param event The event, cannot be null.
	 */
	@Transactional
	public void onApplicationEvent(final MemberInviteEvent event) {
		MemberInviteDto invite = event.getSource();
		log.info("About to start invitation process for " + invite);

		try {
			MemberInfoDto memberInfo = invite.getInvitee();
			MemberManagementService memberManagementService = this.beanFactory
					.getBean(MemberManagementService.class);
			memberManagementService.inviteInitiate(invite.getFirstName(),
					invite.getLastName(), invite.getEmail(), invite.getCompany(),
					invite.getMemberInviteType(), memberInfo.getFirstName(),
					memberInfo.getLastName(), memberInfo.getId());
		} catch (RuntimeException e) {
			log.error("Unable to send invitation " + invite, e);
			throw e;
		}
		log.info("Invite sent successfully");
	}

	/**
	 * @param beanFactory the beanFactory to set
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

}
