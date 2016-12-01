package com.similan.domain.entity.lead;

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

@Entity(name = "CrmLeadSyncRecord")
public class CrmLeadSyncRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;

	@Column
	private Long numberOfNewLeadsFromCrm;

	@Column
	private Long numberOfNewLeadsFromBr;

	@Column
	private Long numberOfUpdatesFromBr;

	@Column
	private Long numberOfUpdatesFromCrm;

	@ManyToOne
	private SocialOrganization owner;

	@Enumerated(EnumType.STRING)
	@Column
	private CrmSystemType crmSource;

	@Column
	private Date timeStamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumberOfNewLeadsFromCrm() {
		return numberOfNewLeadsFromCrm;
	}

	public void setNumberOfNewLeadsFromCrm(Long numberOfNewLeadsFromCrm) {
		this.numberOfNewLeadsFromCrm = numberOfNewLeadsFromCrm;
	}

	public Long getNumberOfNewLeadsFromBr() {
		return numberOfNewLeadsFromBr;
	}

	public void setNumberOfNewLeadsFromBr(Long numberOfNewLeadsFromBr) {
		this.numberOfNewLeadsFromBr = numberOfNewLeadsFromBr;
	}

	public Long getNumberOfUpdatesFromBr() {
		return numberOfUpdatesFromBr;
	}

	public void setNumberOfUpdatesFromBr(Long numberOfUpdatesFromBr) {
		this.numberOfUpdatesFromBr = numberOfUpdatesFromBr;
	}

	public Long getNumberOfUpdatesFromCrm() {
		return numberOfUpdatesFromCrm;
	}

	public void setNumberOfUpdatesFromCrm(Long numberOfUpdatesFromCrm) {
		this.numberOfUpdatesFromCrm = numberOfUpdatesFromCrm;
	}

	public SocialOrganization getOwner() {
		return owner;
	}

	public void setOwner(SocialOrganization owner) {
		this.owner = owner;
	}

	public CrmSystemType getCrmSource() {
		return crmSource;
	}

	public void setCrmSource(CrmSystemType crmSource) {
		this.crmSource = crmSource;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "JpaCrmLeadSyncRecord [id=" + id + ", numberOfNewLeadsFromCrm="
				+ numberOfNewLeadsFromCrm + ", numberOfNewLeadsFromBr="
				+ numberOfNewLeadsFromBr + ", numberOfUpdatesFromBr="
				+ numberOfUpdatesFromBr + ", numberOfUpdatesFromCrm="
				+ numberOfUpdatesFromCrm + ", owner=" + owner.getId() + ", crmSource="
				+ crmSource + ", timeStamp=" + timeStamp + "]";
	}
}
