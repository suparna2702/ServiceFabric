package com.similan.domain.entity.community;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.service.api.connection.dto.basic.ContactType;

@Entity(name="SocialContact")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "SocialContactType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("SocialContact")
public class SocialContact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@JoinColumn
	@ManyToOne
	private SocialActor contactFrom;
	
	@JoinColumn
	@ManyToOne
	private SocialActor contactTo;
	
	@Column
	private String aliasName;
	
	@Column
	private Date created;
	
	@Enumerated(EnumType.STRING)
	@Column
	private ContactType contactType;
	
	@Column
	private Boolean approved;
	
	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SocialActor getContactFrom() {
		return contactFrom;
	}

	public void setContactFrom(SocialActor contactFrom) {
		this.contactFrom = contactFrom;
	}
	
	public SocialActor getContactTo() {
		return contactTo;
	}

	public void setContactTo(SocialActor contactTo) {
		this.contactTo = contactTo;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	

}
