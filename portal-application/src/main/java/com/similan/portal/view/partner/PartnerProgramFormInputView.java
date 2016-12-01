package com.similan.portal.view.partner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.similan.framework.dto.partner.PartnerPreQualificationQuestionDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.portal.view.BaseView;

@ViewScoped
@ManagedBean(name = "partnerProgramFormInputView")
@Slf4j
public class PartnerProgramFormInputView extends BaseView {

	private static final long serialVersionUID = 1L;
	private PartnerProgramDefinitionDto partnerProgram;
	private String processInstanceId = StringUtils.EMPTY;
	private Long memTransStateId = Long.MIN_VALUE;
	private Long partnerOrgId = Long.MIN_VALUE;
	private Long inviterId = Long.MIN_VALUE;
	
	@PostConstruct
	public void init() {
		
		String partnerIdParam = this.getContextParam("prid");
		processInstanceId = this.getContextParam("pid");
		memTransStateId = Long.valueOf(this.getContextParam("msid"));
		partnerOrgId = Long.valueOf(this.getContextParam("oid"));
		inviterId = Long.valueOf(this.getContextParam("mid"));
		log.info("Fetching partner program " + partnerIdParam + " org id " + partnerOrgId + "inviter id " 
		                          + inviterId + " member trans state id " + memTransStateId);
		
		partnerProgram = this.orgService.getPartnerProgramById(Long.valueOf(partnerIdParam));
		sortQuestionsByIndex();
	}

	private void sortQuestionsByIndex() {
		if (partnerProgram == null)
			return;
		List<PartnerPreQualificationQuestionDto> questions = partnerProgram
				.getPreQualificationQuestions();
		if (questions != null)
			Collections.sort(questions,
					new Comparator<PartnerPreQualificationQuestionDto>() {

						public int compare(
								PartnerPreQualificationQuestionDto o1,
								PartnerPreQualificationQuestionDto o2) {
							return o1.getQuestionIndex().compareTo(
									o2.getQuestionIndex());
						}
					});

	}

	public PartnerProgramDefinitionDto getPartnerProgram() {
		return partnerProgram;
	}

	public void setPartnerProgram(PartnerProgramDefinitionDto partnerProgram) {
		this.partnerProgram = partnerProgram;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getMemTransStateId() {
		return memTransStateId;
	}

	public void setMemTransStateId(Long memTransStateId) {
		this.memTransStateId = memTransStateId;
	}

	public Long getPartnerOrgId() {
		return partnerOrgId;
	}

	public void setPartnerOrgId(Long partnerOrgId) {
		this.partnerOrgId = partnerOrgId;
	}

	public Long getInviterId() {
		return inviterId;
	}

	public void setInviterId(Long inviterId) {
		this.inviterId = inviterId;
	}

	public String submitPartnerProgramApprovalFormInput(){
		
		try {
			log.info("Partner program reponse submit");
			this.orgService.submitPartnerProgramApprovalFormInput(partnerProgram, 
					partnerOrgId, memTransStateId, inviterId, processInstanceId);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
                    "Thanks for submitting the partner response "));
		}
		catch(Exception exp){
			log.info("Error submitting the partner response", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Error submitting the partner response " + exp.getMessage()));
		}
		
		return "submissionComplete";
	}
}
