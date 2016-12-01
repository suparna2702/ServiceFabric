package com.similan.domain.entity.lead;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "TransferLeadRequest")
public class TransferLeadRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TransferLead> transferLeads;

	@Column
	private Long fromSocialActorId;

	@Column
	private Long toSocialActorId;

	@Column
	private String workflowProcessId;

	@Column
	private Integer remindersSent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TransferLead> getTransferLeads() {
		return transferLeads;
	}

	public void setTransferLeads(List<TransferLead> transferLeads) {
		this.transferLeads = transferLeads;
	}

	public Long getFromSocialActorId() {
		return fromSocialActorId;
	}

	public void setFromSocialActorId(Long fromSocialActorId) {
		this.fromSocialActorId = fromSocialActorId;
	}

	public Long getToSocialActorId() {
		return toSocialActorId;
	}

	public void setToSocialActorId(Long toSocialActorId) {
		this.toSocialActorId = toSocialActorId;
	}

	public String getWorkflowProcessId() {
		return workflowProcessId;
	}

	public void setWorkflowProcessId(String workflowProcessId) {
		this.workflowProcessId = workflowProcessId;
	}

	public Integer getRemindersSent() {
		return remindersSent;
	}

	public void setRemindersSent(Integer remindersSent) {
		this.remindersSent = remindersSent;
	}
}
