package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.lead.LeadSearchFilterSettingDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadSearchFiltersView")
@Slf4j
public class LeadSearchFiltersView extends BaseView {
  private static final long serialVersionUID = 1L;

	
	private List<LeadSearchFilterSettingDto> searchFilters;
	
	@Autowired
	private MemberInfoDto memberInfo;
	
	@PostConstruct
	public void init() {
		
		log.info("Initializing the search filter view for member " + memberInfo.getFirstName());
		
		try {
			//this.searchFilters = this.getLeadService().getLeadSearchFilterSettings(memberInfo);
		}
		catch(Exception exp){
			log.error("Cannot fetch search filter list ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cannot fetch search filter list ", 
                    "Cannot fetch search filter list "));
		}
				
		if(this.searchFilters == null){
			searchFilters = new ArrayList<LeadSearchFilterSettingDto>();
		}
	}
	
	public List<LeadSearchFilterSettingDto> getSearchFilters() {
		return searchFilters;
	}

	public void setSearchFilters(List<LeadSearchFilterSettingDto> searchFilters) {
		this.searchFilters = searchFilters;
	}

	public MemberInfoDto getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfoDto memberInfo) {
		this.memberInfo = memberInfo;
	}
	
	public void createSearchFilter(){
		log.info("Create new search filter");
		facesHelper.redirect("/lead/leadSearchSettings.xhtml");
	}
	
	public void deleteFilter(Long filterId){
		log.info("Deleting search filter with id " + filterId);
	}
	
	/**
	 * This method should 1) Find the search filter
	 *  2) Then should create a Feedlist and attach 
	 *     it to that search filter
	 *  3) Then store it
	 *  4) When user clicks on that it should be able to 
	 *     navigate to a list view to show all the available lists
	 * @param filterId
	 */
	public void refreshSearch(Long filterId){
		log.info("Refreshing search filter with id " + filterId);
	}
	
}
