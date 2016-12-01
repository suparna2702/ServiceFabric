package com.similan.portal.actions;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.similan.portal.service.MemberService;

@Slf4j
public class MemberSignupCompleteAction implements Action {
    
	
	private MemberService memberService;
	
	public MemberService getMemberService() {
		return memberService;
	}

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * The execution handler
	 */
	public Event execute(RequestContext context) throws Exception {
		
		/* get the http request context */
		ServletExternalContext externalContext = (ServletExternalContext) context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getNativeRequest();
		
		/* get the parameters from it */
		String memberId = request.getParameter("mid");
		String processInstanceId = request.getParameter("pid");
		
		/* now we can initiate the account management service 
		 * directly from here */
		log.info("member sign up complete for member id " 
		                + memberId + " process instance id " + processInstanceId);
		
		return null;
	}

}
