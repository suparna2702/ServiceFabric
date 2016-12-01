package com.similan.portal.validator;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;

@Component
public class MemberInfoValidator {
	public void validateMemberSignUpAction(MemberInfoDto memberInfo, ValidationContext context) {
		
		System.out.println("Member email :" 
                + memberInfo.getEmail());
		
        MessageContext messages = context.getMessageContext();
        
        if (memberInfo.getEmail() == null) {
            messages.addMessage(new MessageBuilder().error().source("email").
                defaultText("Email address cannot be null").build());
        } 
        else if (memberInfo.getPassword() == null) {
            messages.addMessage(new MessageBuilder().error().source("password").
                defaultText("Password cannot be null").build());
        }
    }
	
	public void validateMemberLoginAction(MemberInfoDto memberInfo, ValidationContext context) {
		
        MessageContext messages = context.getMessageContext();
        
        if (memberInfo.getUserName() == null) {
            messages.addMessage(new MessageBuilder().error().source("userNmae").
                defaultText("User name cannot be null").build());
        } 
        else if (memberInfo.getPassword() == null) {
            messages.addMessage(new MessageBuilder().error().source("password").
                defaultText("Password cannot be null").build());
        }
    }


}
