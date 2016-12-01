package com.similan.domain.entity.partner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;

@Entity(name="PartnerProgramDefinition")
public class PartnerProgramDefinition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String name;
	
	@Column(length=5000)
	private String description;
	
	@Column(length=10000)
	private String programDetails;
	
	@Column
	private int outStandingInvites;
	
	@Column
	private int totalActiveMembers;
	
	@Column
	private String logoLocation;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "signupOption")
	private PartnerSignupOption signupOptions = PartnerSignupOption.DocumentAndQuestions;
	
	@JoinColumn
	@OneToMany
	private List<PartnerProgramTypeInfo> partnerProgramType;
	
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL)
	private List<PartnerProgramBenifit> programBenifit;
	
	@JoinColumn
	@OneToMany
	private List<PartnerRelationshipCategory> relationshipCategory;
	
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL)
	private List<PartnerProgramCommunicationAttribute> communationAttribute;
	
	@Column
	private Boolean showPreQualificationQuestions = Boolean.FALSE;
	
	@Column
	private Boolean requiredBusinessMemberInfoOption = Boolean.FALSE;
	
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL)
	private List<PartnerPreQualificationQuestion> partnerPreQualificationQuestion = new ArrayList<PartnerPreQualificationQuestion>();
	
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL)
	private List<PartnerProgramRequiredAttribute> partnerProgramAttributes = new ArrayList<PartnerProgramRequiredAttribute>();
	
	@JoinColumn
	@ManyToOne
	private SocialOrganization programOwner;
	
	@JoinColumn
	@ManyToOne
	private SocialActor creator;
	
	@JoinColumn
	@ManyToOne
	private CollaborationWorkspace partnerProgramWorkspace;
	
	@Column
	private Date creationDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public SocialActor getCreator() {
		return creator;
	}

	public void setCreator(SocialActor creator) {
		this.creator = creator;
	}

	public CollaborationWorkspace getPartnerProgramWorkspace() {
		return partnerProgramWorkspace;
	}

	public void setPartnerProgramWorkspace(
			CollaborationWorkspace partnerProgramWorkspace) {
		this.partnerProgramWorkspace = partnerProgramWorkspace;
	}

	public String getProgramDetails() {
		return programDetails;
	}

	public void setProgramDetails(String programDetails) {
		this.programDetails = programDetails;
	}

	public List<PartnerProgramCommunicationAttribute> getCommunationAttribute() {
		return communationAttribute;
	}

	public void setCommunationAttribute(
			List<PartnerProgramCommunicationAttribute> communationAttribute) {
		this.communationAttribute = communationAttribute;
	}

	public List<PartnerProgramBenifit> getProgramBenifit() {
		if(programBenifit == null){
			programBenifit = new ArrayList<PartnerProgramBenifit>();
		}
		return programBenifit;
	}

	public void setProgramBenifit(List<PartnerProgramBenifit> programBenifit) {
		this.programBenifit = programBenifit;
	}

	public List<PartnerRelationshipCategory> getRelationshipCategory() {
		if(relationshipCategory == null){
			relationshipCategory = new ArrayList<PartnerRelationshipCategory>();
		}
		return relationshipCategory;
	}

	public void setRelationshipCategory(
			List<PartnerRelationshipCategory> relationshipCategory) {
		this.relationshipCategory = relationshipCategory;
	}

	public List<PartnerProgramTypeInfo> getPartnerProgramType() {
		return partnerProgramType;
	}

	public void setPartnerProgramType(List<PartnerProgramTypeInfo> partnerProgramType) {
		this.partnerProgramType = partnerProgramType;
	}
	
	public List<PartnerPreQualificationQuestion> getPartnerPreQualificationQuestion() {
		if(partnerPreQualificationQuestion == null){
			partnerPreQualificationQuestion = new ArrayList<PartnerPreQualificationQuestion>();
		}
		return partnerPreQualificationQuestion;
	}

	public void setPartnerPreQualificationQuestion(
			List<PartnerPreQualificationQuestion> partnerPreQualificationQuestion) {
		this.partnerPreQualificationQuestion = partnerPreQualificationQuestion;
	}

	public int getTotalActiveMembers() {
		return totalActiveMembers;
	}

	public void setTotalActiveMembers(int totalActiveMembers) {
		this.totalActiveMembers = totalActiveMembers;
	}

	public PartnerSignupOption getSignupOptions() {
		return signupOptions;
	}

	public void setSignupOptions(PartnerSignupOption signupOptions) {
		this.signupOptions = signupOptions;
	}

	public Boolean getShowPreQualificationQuestions() {
		return showPreQualificationQuestions;
	}

	public void setShowPreQualificationQuestions(
			Boolean showPreQualificationQuestions) {
		this.showPreQualificationQuestions = showPreQualificationQuestions;
	}

	public Boolean getRequiredBusinessMemberInfoOption() {
		return requiredBusinessMemberInfoOption;
	}

	public void setRequiredBusinessMemberInfoOption(
			Boolean requiredBusinessMemberInfoOption) {
		this.requiredBusinessMemberInfoOption = requiredBusinessMemberInfoOption;
	}

	public SocialOrganization getProgramOwner() {
		return programOwner;
	}

	public void setProgramOwner(SocialOrganization programOwner) {
		this.programOwner = programOwner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOutStandingInvites() {
		return outStandingInvites;
	}

	public void setOutStandingInvites(int outStandingInvites) {
		this.outStandingInvites = outStandingInvites;
	}

	public String getLogoLocation() {
		return logoLocation;
	}

	public void setLogoLocation(String logoLocation) {
		this.logoLocation = logoLocation;
	}

	public List<PartnerPreQualificationQuestion> getPartnerPreQualificationQuestions() {
		return partnerPreQualificationQuestion;
	}

	public void setPartnerPreQualificationQuestions(
			List<PartnerPreQualificationQuestion> preQualQuestions) {
		this.partnerPreQualificationQuestion = preQualQuestions;
	}

	public List<PartnerProgramRequiredAttribute> getPartnerProgramAttributes() {
		return partnerProgramAttributes;
	}

	public void setPartnerProgramAttributes(
			List<PartnerProgramRequiredAttribute> partnerProgramAttributes) {
		this.partnerProgramAttributes = partnerProgramAttributes;
	}

}
