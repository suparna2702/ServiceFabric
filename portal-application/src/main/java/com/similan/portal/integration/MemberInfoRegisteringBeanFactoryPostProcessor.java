package com.similan.portal.integration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;
import com.similan.service.api.MemberManagementService;

@Slf4j
public class MemberInfoRegisteringBeanFactoryPostProcessor implements
		BeanFactoryPostProcessor, BeanPostProcessor {


	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		log.info("Registering member info object factory");
		MemberInfoFactory objectFactory = new MemberInfoFactory();
		beanFactory.autowireBean(objectFactory);
		log.info("ObjectFactory successfully autowired");
//		beanFactory.registerResolvableDependency(MemberInfo.class, objectFactory);
		log.info("Object factory registered successfully");
	}

	/**
	 * {@link ObjectFactory} implementation that obtains a member info based
	 * on the currently logged in member.
	 * @author psaavedra
	 */
	public static class MemberInfoFactory implements ObjectFactory<MemberInfoDto> {

		@Autowired
		private MemberManagementService managementService;

		/* (non-Javadoc)
		 * @see org.springframework.beans.factory.ObjectFactory#getObject()
		 */
		public MemberInfoDto getObject() throws BeansException {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			log.info("Obtained current authentication object {}", authentication);
			if (authentication == null) {
				return null;
			}
			
			Object details = authentication.getPrincipal();
			if (!(details instanceof PersistentUser)) {
				return null;
			}
			
			PersistentUser user = (PersistentUser) details;
			log.info("Currently logged in user is {}", user.getUsername());
			
			try {
				
				if(user.getMemberInfo() == null){
					log.info("logged in member is null fetching member from DB " + user.getUsername());
					MemberInfoDto memberInfo = this.managementService.memberById(user.getId());
					user.setMemberInfo(memberInfo);
				}
				
				return user.getMemberInfo();
				
			} catch (Exception e) {
				log.error("Could not find a corresponding member for id "
						+ user.getId(), e);
				return null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	
}
