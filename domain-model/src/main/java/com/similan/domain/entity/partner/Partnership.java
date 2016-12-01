package com.similan.domain.entity.partner;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.SocialOrganization;

@Entity(name="Partnership")
public class Partnership {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private PartnerProgramDefinition partnerProgram;
	
	@ManyToOne
	private SocialOrganization partner;
	
	@ManyToOne
	private PartnerProgramResponse response;
	
	@Column
	private Date created;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PartnershipStatus partnershipStatus = PartnershipStatus.Pending;

	@Column
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PartnershipStatus getPartnershipStatus() {
		return partnershipStatus;
	}

	public void setPartnershipStatus(PartnershipStatus partnershipStatus) {
		this.partnershipStatus = partnershipStatus;
	}

	public PartnerProgramResponse getResponse() {
		return response;
	}

	public void setResponse(PartnerProgramResponse response) {
		this.response = response;
	}

	public PartnerProgramDefinition getPartnerProgram() {
		return partnerProgram;
	}

	public void setPartnerProgram(PartnerProgramDefinition partnerProgram) {
		this.partnerProgram = partnerProgram;
	}

	public SocialOrganization getPartner() {
		return partner;
	}

	public void setPartner(SocialOrganization partner) {
		this.partner = partner;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
