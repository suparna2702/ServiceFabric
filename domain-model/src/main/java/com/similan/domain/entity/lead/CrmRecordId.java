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

@Entity(name = "CrmRecordId")
public class CrmRecordId {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;

	@Column
	private String remoteId;

	@Column
	private Date lastRemoteTimestamp;

	@Column
	private Long localId;

	@Column
	private Date lastLocalTimestamp;

	@Enumerated(EnumType.STRING)
	@Column
	private CrmSystemType crmSource;

	@Enumerated(EnumType.STRING)
	@Column
	private CrmRecordType crmRecordType;

	@ManyToOne
	private SocialOrganization owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public Date getLastRemoteTimestamp() {
		return lastRemoteTimestamp;
	}

	public void setLastRemoteTimestamp(Date lastRemoteTimestamp) {
		this.lastRemoteTimestamp = lastRemoteTimestamp;
	}

	public Long getLocalId() {
		return localId;
	}

	public void setLocalId(Long localId) {
		this.localId = localId;
	}

	public Date getLastLocalTimestamp() {
		return lastLocalTimestamp;
	}

	public void setLastLocalTimestamp(Date lastLocalTimestamp) {
		this.lastLocalTimestamp = lastLocalTimestamp;
	}

	public CrmSystemType getCrmSource() {
		return crmSource;
	}

	public void setCrmSource(CrmSystemType crmSource) {
		this.crmSource = crmSource;
	}

	public CrmRecordType getCrmRecordType() {
		return crmRecordType;
	}

	public void setCrmRecordType(CrmRecordType crmRecordType) {
		this.crmRecordType = crmRecordType;
	}

	public SocialOrganization getOwner() {
		return owner;
	}

	public void setOwner(SocialOrganization owner) {
		this.owner = owner;
	}

}
