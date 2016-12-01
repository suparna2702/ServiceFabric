package com.similan.portal.databean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.portal.view.BaseView;

@ManagedBean(name = "partnerProgramBean")
@ApplicationScoped
public class PartnerProgramBean extends BaseView {
	private static final long serialVersionUID = 1L;
	
	private List<PartnerProgramDefinitionDto> partnerPrograms;
	private Map<Long, PartnerProgramDefinitionDto> lookupMapById;
	
	@PostConstruct
	public void init() {
		this.partnerPrograms = orgService.getPartnerPrograms();
		
		if(this.partnerPrograms  != null){
			this.lookupMapById = new HashMap<Long, PartnerProgramDefinitionDto>(); 
			
			for(PartnerProgramDefinitionDto program : this.partnerPrograms){
				this.lookupMapById.put(program.getId(), program);
			}
		}
	}
	
	public PartnerProgramDefinitionDto getPartnerProgramById(Long id){
		PartnerProgramDefinitionDto retProgram = this.lookupMapById.get(id);
		if(retProgram == null){
			retProgram = this.orgService.getPartnerProgramById(id);
		}
		
		return retProgram;
	}
	
	public List<PartnerProgramDefinitionDto> getPartnerProgramsByExcludeOrgFilter(Long orgId){
		List<PartnerProgramDefinitionDto> filteredList = new ArrayList<PartnerProgramDefinitionDto>();
		
		for(PartnerProgramDefinitionDto program : this.partnerPrograms){
			if(program.getOwner().getId() != orgId){
				filteredList.add(program);
			}
		}
		
		return filteredList;
	}

	public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
		return partnerPrograms;
	}

	public void setPartnerPrograms(List<PartnerProgramDefinitionDto> partnerPrograms) {
		this.partnerPrograms = partnerPrograms;
	}

}
