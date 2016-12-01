package com.similan.service.api;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.framework.dto.member.MemberInfoDto;

/**
 * This messaging service would abstract sending message
 * to email, mobile destination
 * @author supapal
 *
 */
public interface MessagingService {
	
	/**
	 * Send Email
	 * @param sender
	 * @param toEmailAddress
	 * @param subject
	 * @param emailTemplateName
	 * @param arguments
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws Exception
	 */
	public void sendContactMessageToMember(ContactMessageDetail message, 
            MemberInfoDto loggedMember, 
            MemberInfoDto memberToSend) throws ResourceNotFoundException,
			ParseErrorException, Exception;

}
