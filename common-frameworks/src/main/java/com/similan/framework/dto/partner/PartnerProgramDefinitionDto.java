package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DualListModel;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.partner.PartnerSignupOption;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.util.DefaultImageConatantDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;

public class PartnerProgramDefinitionDto implements Serializable {

  
  private static final long serialVersionUID = 1L;

  private long id;

  private String logoLocation;

  private String name;

  private OrganizationDetailInfoDto owner;

  private MemberInfoDto creator;

  private String description;

  private String programDetails;

  private int totalActiveMembers;

  private int outStandingInvites;

  private boolean showPreQualificationQuestions;

  private boolean requiredBusinessMemberInfoOption;

  private boolean select;

  private PartnerSignupOption signupOptions = PartnerSignupOption.DocumentAndQuestions;

  private List<PartnerPreQualificationQuestionDto> preQualQuestions;

  private List<PartnerPreQualificationQuestionDto> deletedPreQualQuestions;

  private PartnerPreQualificationQuestionDto selectedPreQualQuestions;

  private DualListModel<PartnerRequiredAttributeDto> partnerRequiredInfoAttributes;

  private List<PartnerRequiredAttributeDto> partnerProgramAttributes;

  private List<PartnerProgramBenifitDto> partnerProgramBenifits;

  private List<PartnerRelationshipCategoryDto> partnerRelationshipCategory;

  private CollaborationWorkspaceDto workspace = null;

  public PartnerProgramDefinitionDto() {

    id = Long.MIN_VALUE;
    preQualQuestions = new ArrayList<PartnerPreQualificationQuestionDto>();
    selectedPreQualQuestions = new PartnerPreQualificationQuestionDto();
    partnerProgramAttributes = new ArrayList<PartnerRequiredAttributeDto>();
    deletedPreQualQuestions = new ArrayList<PartnerPreQualificationQuestionDto>();
  }

  public MemberInfoDto getCreator() {
    return creator;
  }

  public void setCreator(MemberInfoDto creator) {
    this.creator = creator;
  }

  public CollaborationWorkspaceDto getWorkspace() {
    return workspace;
  }

  public void setWorkspace(CollaborationWorkspaceDto workspace) {
    this.workspace = workspace;
  }

  public List<PartnerRelationshipCategoryDto> getPartnerRelationshipCategory() {
    if (partnerRelationshipCategory == null) {
      partnerRelationshipCategory = new ArrayList<PartnerRelationshipCategoryDto>();
    }
    return partnerRelationshipCategory;
  }

  public void setPartnerRelationshipCategory(
      List<PartnerRelationshipCategoryDto> partnerRelationshipCategory) {
    this.partnerRelationshipCategory = partnerRelationshipCategory;
  }

  public List<PartnerProgramBenifitDto> getPartnerProgramBenifits() {
    if (this.partnerProgramBenifits == null) {
      this.partnerProgramBenifits = new ArrayList<PartnerProgramBenifitDto>();
    }
    return partnerProgramBenifits;
  }

  public void setPartnerProgramBenifits(
      List<PartnerProgramBenifitDto> partnerProgramBenifits) {
    this.partnerProgramBenifits = partnerProgramBenifits;
  }

  public String getProgramDetails() {
    return programDetails;
  }

  public void setProgramDetails(String programDetails) {
    this.programDetails = programDetails;
  }

  public List<PartnerPreQualificationQuestionDto> getDeletedPreQualQuestions() {
    return deletedPreQualQuestions;
  }

  public void setDeletedPreQualQuestions(
      List<PartnerPreQualificationQuestionDto> deletedPreQualQuestions) {
    this.deletedPreQualQuestions = deletedPreQualQuestions;
  }

  public List<PartnerRequiredAttributeDto> getPartnerProgramAttributes() {
    return partnerProgramAttributes;
  }

  public void setPartnerProgramAttributes(
      List<PartnerRequiredAttributeDto> partnerProgramAttributes) {
    this.partnerProgramAttributes = partnerProgramAttributes;
  }

  public void setSignupOptions(PartnerSignupOption signupOptions) {
    this.signupOptions = signupOptions;
  }

  public OrganizationDetailInfoDto getOwner() {
    return owner;
  }

  public boolean isSelect() {
    return select;
  }

  public void setSelect(boolean select) {
    this.select = select;
  }

  public void setOwner(OrganizationDetailInfoDto owner) {
    this.owner = owner;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public DualListModel<PartnerRequiredAttributeDto> getPartnerRequiredInfoAttributes() {
    return partnerRequiredInfoAttributes;
  }

  public void setPartnerRequiredInfoAttributes(
      DualListModel<PartnerRequiredAttributeDto> partnerRequiredInfoAttributes) {
    this.partnerRequiredInfoAttributes = partnerRequiredInfoAttributes;
  }

  public boolean isRequiredBusinessMemberInfoOption() {
    return requiredBusinessMemberInfoOption;
  }

  public void setRequiredBusinessMemberInfoOption(
      boolean requiredBusinessMemberInfoOption) {
    this.requiredBusinessMemberInfoOption = requiredBusinessMemberInfoOption;
  }

  public PartnerPreQualificationQuestionDto getSelectedPreQualificationQuestion() {
    return selectedPreQualQuestions;
  }

  public void setSelectedPreQualificationQuestion(
      PartnerPreQualificationQuestionDto selectedPreQualQuestions) {
    this.selectedPreQualQuestions = selectedPreQualQuestions;
  }

  public List<PartnerPreQualificationQuestionDto> getPreQualificationQuestions() {
    return preQualQuestions;
  }

  public void setPreQualificationQuestions(
      List<PartnerPreQualificationQuestionDto> preQualQuestions) {
    this.preQualQuestions = preQualQuestions;
  }

  public boolean isShowPreQualificationQuestions() {
    return showPreQualificationQuestions;
  }

  public void setShowPreQualificationQuestions(
      boolean showPreQualificationQuestions) {
    this.showPreQualificationQuestions = showPreQualificationQuestions;
  }

  public String getSignupOptions() {
    return signupOptions.toString();
  }

  public void setSignupOptions(String options) {
    signupOptions = PartnerSignupOption.valueOf(options);
  }

  public int getTotalActiveMembers() {
    return totalActiveMembers;
  }

  public void setTotalActiveMembers(int totalActiveMembers) {
    this.totalActiveMembers = totalActiveMembers;
  }

  public int getOutStandingInvites() {
    return outStandingInvites;
  }

  public void setOutStandingInvites(int outStandingInvites) {
    this.outStandingInvites = outStandingInvites;
  }

  public String getLogoLocation() {
    return PhotoViewOption.ShowPhoto.effectivePhoto(DefaultImageConatantDto.IMAGES_PRODUCT_DEFAULT_LOGO,
        this.logoLocation);
  }

  public void setLogoLocation(String logoLocation) {
    this.logoLocation = logoLocation;
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

}
