package com.similan.framework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

@Slf4j
public class OrganizationBasicInfoList implements Serializable {

	private static final long serialVersionUID = 1L;
			
	private List<OrganizationBasicInfoDto> orgList = null;
	
	public OrganizationBasicInfoList(List<OrganizationBasicInfoDto> orgListParam){
		this.orgList = orgListParam;
	}

	public List<OrganizationBasicInfoDto> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<OrganizationBasicInfoDto> orgList) {
		this.orgList = orgList;
	}
	
	public List<OrganizationBasicInfoDto> autoSuggestOrg(String query){

		log.info("completeOrgTag of OrganizationAutoCompleteBean " + query);
		List<OrganizationBasicInfoDto> suggestions = new ArrayList<OrganizationBasicInfoDto>();
		if (query == null)
			return suggestions;


		if (orgList != null) {
			for (OrganizationBasicInfoDto orgTag : orgList) {
				if (StringUtils.startsWithIgnoreCase(orgTag.getName(), query)
						|| StringUtils.containsIgnoreCase(orgTag.getName(),
								query))
					suggestions.add(orgTag);
			}
		}
        
		log.info("Suggestions found in OrganizationAutoCompleteBean " + suggestions);
        return suggestions;  
	}
}
