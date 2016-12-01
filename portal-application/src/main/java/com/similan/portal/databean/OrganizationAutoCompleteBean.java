package com.similan.portal.databean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.portal.view.BaseView;

@SessionScoped
@ManagedBean(name = "orgAutoCompleteBean")
@Slf4j
public class OrganizationAutoCompleteBean extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private List<OrganizationBasicInfoDto> orgList = null;
	
	private OrganizationBasicInfoDto selectedTag = null;
	
	@PostConstruct
	public void init() {
		
		/**
		 * Get all organization and create Tag
		 */
		orgList = orgService.getVisibleBusinesses();
		log.info("Post init of OrganizationAutoCompleteBean " + orgList);
	}
	
	public OrganizationBasicInfoDto getOrgByTagName(String name){
		OrganizationBasicInfoDto retOrgTag = null;
		
		if(orgList != null){
			for(OrganizationBasicInfoDto tag : orgList){
				if(tag.getName().equalsIgnoreCase(name) == true){
					log.info("Matching business found " + tag.getName());
					retOrgTag = tag;
					break;
				}
			}
		}
		
		return retOrgTag;
	}
	
	public OrganizationBasicInfoDto getSelectedTag() {
		return selectedTag;
	}

	public void setSelectedTag(OrganizationBasicInfoDto selectedTag) {
		this.selectedTag = selectedTag;
	}

	public List<OrganizationBasicInfoDto> completeOrgTag(String query){
		
		log.info("completeOrgTag of OrganizationAutoCompleteBean " + query);
		List<OrganizationBasicInfoDto> suggestions = new ArrayList<OrganizationBasicInfoDto>();  
        
		if(orgList != null){
	        for(OrganizationBasicInfoDto orgTag : orgList) {  
	            if(orgTag.getName().startsWith(query) 
	            		|| orgTag.getName().contains(query))  
	                suggestions.add(orgTag);  
	        }  
		}
        
		log.info("Suggestions found in OrganizationAutoCompleteBean " + suggestions);
        return suggestions;  
	}

	public List<OrganizationBasicInfoDto> getOrgList() {
		//Here we are trying to update the org list from data base 
		//in case of new org. Later with 2nd level cache this should be automatic
		orgList = orgService.getVisibleBusinesses();
		return orgList;
	}

	public void setOrgList(List<OrganizationBasicInfoDto> orgList) {
		this.orgList = orgList;
	}

}
