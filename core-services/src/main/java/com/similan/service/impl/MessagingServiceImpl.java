package com.similan.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.repository.template.VelocityTemplateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadMessageDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.manager.email.EmailManagerImpl;
import com.similan.service.api.MessagingService;
import com.similan.service.exception.CoreServiceException;

@Slf4j
public class MessagingServiceImpl implements MessagingService {
  @Autowired
  private EmailManagerImpl emailManager;
  @Autowired
  private VelocityTemplateRepository templateRepository;
  @Autowired
  private PlatformCommonSettings commonSettings;

	public void sendNewPasswordToMember(String newEmailAddr, Long memberId){
		log.info("New email to be send " + newEmailAddr + " member id " + memberId);
	}
	
	/**
	 * 
	 * @param leadMessage
	 * @param lead
	 * @throws Exception 
	 * @throws ParseErrorException 
	 * @throws ResourceNotFoundException 
	 */
	public void sendLeadMessage(LeadMessageDto leadMessageDto, LeadDto leadDto, 
			MemberInfoDto sender) throws ResourceNotFoundException, ParseErrorException, Exception {
		
		if(leadMessageDto == null || leadDto == null || sender == null){
			throw new CoreServiceException("Cannot send email due to null argument");
		}
		
		log.info("Sending message to lead " + leadDto.getContactEmail());
		if(StringUtils.isEmpty(leadDto.getContactEmail()) ==  true){
			throw new CoreServiceException("Cannot send email to this lead with invalid email address");
		}
		
		if(StringUtils.isEmpty(sender.getEmail()) ==  true){
			throw new CoreServiceException("Cannot send email to this lead since member has an invalid email address");
		}
		
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("fromName", sender.getFirstName());
		arguments.put("toName", leadDto.getFirstName());
		arguments.put("messageContent", leadMessageDto
				             .getLeadNote().getMessage());
		this.emailManager.send(commonSettings.getPlatformEmailSenderAddress().getValue(), 
				leadDto.getContactEmail(), "leadContactMessageTemplate.vm", arguments);
	}
	
	public void sendContactMessageToOrganization(ContactMessageDetail message, 
            MemberInfoDto loggedMember, 
            OrganizationDetailInfoDto orgToSend) throws ResourceNotFoundException,
			ParseErrorException, Exception {
		
		log.info("Sending message to org " + orgToSend.getId());
		
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("fromMemberName", loggedMember.getFirstName());
		arguments.put("toMemberName", orgToSend.getBusinessName());
		arguments.put("fromMemberId", loggedMember.getId().toString());
		arguments.put("messageContent", message.getMessage());
		
		StringBuilder memberProfileUrl = new StringBuilder();
		memberProfileUrl.append(commonSettings.getPortalApplcationUrl().getValue())
		                .append("business/businessPublicProfile.xhtml?").append("mid=")
		                .append(loggedMember.getId()).append("&loid=").append(orgToSend.getId());
		arguments.put("memberProfileUrl", memberProfileUrl.toString());
		
		this.emailManager.send(commonSettings.getPlatformEmailSenderAddress().getValue(), 
				orgToSend.getBusinessPrimaryEmail(), "memberContactMessageTemplate.vm", arguments);

	}

	public void sendContactMessageToMember(ContactMessageDetail message, 
            MemberInfoDto loggedMember, 
            MemberInfoDto memberToSend) throws ResourceNotFoundException,
			ParseErrorException, Exception {
		
		log.info("Sending message to member " + memberToSend.getId());
		
		Map<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("subject", message.getSubject());
    arguments.put("fromMemberName", loggedMember.getFirstName());
		arguments.put("toMemberName", memberToSend.getFirstName());
		arguments.put("fromMemberId", loggedMember.getId().toString());
		arguments.put("messageContent", message.getMessage());
		
		StringBuilder memberProfileUrl = new StringBuilder();
		memberProfileUrl.append(commonSettings.getPortalApplcationUrl().getValue())
		                .append("member/memberPublicProfile.xhtml?").append("mid=")
		                .append(loggedMember.getId()).append("&lmid=").append(memberToSend.getId());
		arguments.put("memberProfileUrl", memberProfileUrl.toString());
		
		this.emailManager.send(commonSettings.getPlatformEmailSenderAddress().getValue(), 
				memberToSend.getEmail(), 
				"memberContactMessageTemplate.vm", arguments);

	}

}
