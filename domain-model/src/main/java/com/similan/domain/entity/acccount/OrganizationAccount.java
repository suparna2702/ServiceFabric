package com.similan.domain.entity.acccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.community.SocialOrganization;

@Entity(name="OrganizationAccount")
public class OrganizationAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@OneToMany
	private List<AccountRecord> accountRecords;
	
	@ManyToOne
	private SocialOrganization owner;
	
	@Column
	private Date timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public SocialOrganization getOwner() {
		return owner;
	}

	public void setOwner(SocialOrganization owner) {
		this.owner = owner;
	}

	public List<AccountRecord> getAccountRecords() {
		if(accountRecords == null){
			accountRecords = new ArrayList<AccountRecord>();
		}
		return accountRecords;
	}

	public void setAccountRecords(List<AccountRecord> accountRecords) {
		this.accountRecords = accountRecords;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
