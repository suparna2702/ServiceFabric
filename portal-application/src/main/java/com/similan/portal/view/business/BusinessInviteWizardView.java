package com.similan.portal.view.business;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.FlowEvent;

import com.similan.domain.entity.metadata.StateType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.databean.CountryBean;
import com.similan.portal.view.BaseView;

@ViewScoped
@ManagedBean(name = "businessInviteWizard")
@Slf4j
public class BusinessInviteWizardView extends BaseView {

	private static final long serialVersionUID = 1L;
			
	private MemberInfoDto member = null;
	
	private OrganizationDetailInfoDto orgInfo = new OrganizationDetailInfoDto();
	
	private String processInstanceId;
	
	private List<StateType> selectedStateList = null;
	
	@PostConstruct
	public void init() {
		String memberValidationId = this.getContextParam("mid");
		long id = Long.parseLong(memberValidationId);
		processInstanceId = this.getContextParam("pid");
		try {
			member = memberService.getMemberById(Long
					.valueOf(memberValidationId));
		} catch (Exception e) {
		}
		if (member == null) {
			member = new MemberInfoDto();
			member.setId(id);
		}
		
		this.loadStatesForSelectedCountry();
		log.info("Post construct of BusinessInviteWizardView " + id + " pid " + processInstanceId);

	}
	
	public void loadStatesForSelectedCountry(){
		CountryBean countryBean = (CountryBean)this.findBackingBean("countryBean");	
		this.selectedStateList = countryBean.getStateTypeList("United States");
		
		log.info("Country " + member.getCountry() + " state list " + this.selectedStateList.size() + 
				" content " + this.selectedStateList.get(0).getStateName());
	}

	
	public List<StateType> getSelectedStateList() {
		return selectedStateList;
	}

	public void setSelectedStateList(List<StateType> selectedStateList) {
		this.selectedStateList = selectedStateList;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}
	
	public void submitBusinessInfo(){
		log.info("Submitting member info " + member.getFirstName() + " business " 
	                                   + orgInfo.getBusinessName());
		try{
			this.orgService.businessInviteValidateBusinessAttrs(member, orgInfo, processInstanceId);
			facesHelper.redirect("/business/businessInviteInformationComplete.xhtml");

		}
		catch(Exception exp){
			log.error("Error submitting the business input ", exp);
		}
	}
	
	public String onFlowProcess(FlowEvent event) { 
		
        String oldStep = event.getOldStep();
        String newStep = event.getNewStep();
        
        log.info("Current wizard step: " + oldStep + " new step " + newStep);
        if(oldStep.equalsIgnoreCase("business") == true){
        	newStep = "confirmation";
        }
        
        return newStep;  
    }  
	
}
