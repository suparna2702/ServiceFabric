package com.similan.portal.view.partner;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnershipDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("partnershipDetailView")
@Slf4j
public class PartnershipDetailView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	
	private List<PartnershipDto> partnerships;
	private PartnerProgramDefinitionDto program;
	
	@PostConstruct
	public void init() {
		
		String partnerId = this.getContextParam("pid");
		log.info("Partnership detail view init for " + partnerId);
		Long pid = Long.valueOf(partnerId);
		this.partnerships = this.getOrgService().getPartnersForProgram(pid);
		this.program = this.getOrgService().getPartnerProgramById(pid);
	}
	
	public PartnerProgramDefinitionDto getProgram() {
		return program;
	}

	public void setProgram(PartnerProgramDefinitionDto program) {
		this.program = program;
	}



	public List<PartnershipDto> getPartnerships() {
		return partnerships;
	}

	public void setPartnerships(List<PartnershipDto> partnerships) {
		this.partnerships = partnerships;
	}
}
