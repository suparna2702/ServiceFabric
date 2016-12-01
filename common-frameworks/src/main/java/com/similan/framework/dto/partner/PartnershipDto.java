package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.Date;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.partner.PartnershipStatus;
import com.similan.framework.dto.OrganizationBasicInfoDto;

public class PartnershipDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private PartnerProgramDefinitionDto program;
	
	private OrganizationBasicInfoDto orgPartner;
	
	private PartnershipStatus partnershipStatus = PartnershipStatus.Pending;
	
	private PartnerProgramResponseDto response;
	
	private Date created;
	
	private String comment;
	
	public PartnershipDto(){
		
		id = Long.MIN_VALUE;
		partnershipStatus = PartnershipStatus.Pending;
		created = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public PartnerProgramResponseDto getResponse() {
		return response;
	}

	public void setResponse(PartnerProgramResponseDto response) {
		this.response = response;
	}

	public PartnershipStatus getPartnershipStatus() {
		return partnershipStatus;
	}

	public void setPartnershipStatus(PartnershipStatus partnershipStatus) {
		this.partnershipStatus = partnershipStatus;
	}

	public PartnerProgramDefinitionDto getProgram() {
		return program;
	}

	public void setProgram(PartnerProgramDefinitionDto program) {
		this.program = program;
	}

	public OrganizationBasicInfoDto getOrgPartner() {
		return orgPartner;
	}

	public void setOrgPartner(OrganizationBasicInfoDto orgPartner) {
		this.orgPartner = orgPartner;
	}
	
	public void setPartnerLogo(String logo){
		this.orgPartner.setLogoLocation(logo);
	}
	
	public String getPartnerLogo(){
		return PhotoViewOption.ShowPhoto.effectivePhoto("/images/businessLogo.jpg", 
				                                      this.orgPartner.getLogoLocation());
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
