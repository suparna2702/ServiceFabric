package com.similan.domain.entity.leadcapture;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.community.SocialOrganization;

@Entity(name="LeadCaptureWizzardTemplate")
public class LeadCaptureWizzardTemplate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Column
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column
	private String emailAddress;
	
	@Column
	private String phoneNumber;
	
	@Column
	private String templateName;
	
	@OneToMany
	private List<LeadCaptureQuestion> questions;
	
	@ManyToOne
	private SocialOrganization owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public SocialOrganization getOwner() {
		return owner;
	}

	public void setOwner(SocialOrganization owner) {
		this.owner = owner;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<LeadCaptureQuestion> getQuestions() {
		
		if(questions == null){
			questions = new ArrayList<LeadCaptureQuestion>();
		}
		return questions;
	}

	public void setQuestions(List<LeadCaptureQuestion> questions) {
		this.questions = questions;
	}

}
