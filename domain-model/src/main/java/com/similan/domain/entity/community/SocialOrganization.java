package com.similan.domain.entity.community;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.entity.product.Product;

@Entity(name = "SocialOrganization")
@DiscriminatorValue("SocialOrganization")
@Getter
@Setter
public class SocialOrganization extends SocialActor {

  @Column
  private String businessHome;

  @Column(length = 5000)
  private String businessDescription;

  @Column
  private String logoUrl;

  @Column
  private String websiteUrl;

  @Column
  private String communityLandingPageUrl;

  @Column
  private String industry;

  @Column
  private String businessName;

  @Enumerated(EnumType.STRING)
  @Column
  private PhotoViewOption logoViewOptionType = PhotoViewOption.HidePhoto;

  @Enumerated(EnumType.STRING)
  @Column
  private OrganizationLocationShowPreferenceType locationPreferenceType = OrganizationLocationShowPreferenceType.All;

  @Enumerated(EnumType.STRING)
  @Column
  private OrganizationLocationGroupingPrefType locationGrpPrefType = OrganizationLocationGroupingPrefType.FuntionalHQ_SalesOffices;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<OrganizationSearchPreference> searchPreferences;

  @Column
  private String primaryEmail;

  @Column
  private String businessTaxId;

  @Column
  private String businessStateTaxId;

  @Column
  private String businessPhoneNumber;

  @Column
  private Date creationDate;

  @Enumerated(EnumType.STRING)
  @Column
  private AssociateShowOptionType associateShowPublicOpt = AssociateShowOptionType.All;

  @Enumerated(EnumType.STRING)
  @Column
  private OrganizationType organizationType = OrganizationType.UNSPECIFIED;

  @Enumerated(EnumType.STRING)
  @Column
  private AssociateShowOptionType associateShowCommunityOpt = AssociateShowOptionType.All;

  @Enumerated(EnumType.STRING)
  @Column
  private OrganizationStatus status = OrganizationStatus.Pending;

  @Column
  private String productPageUrl;

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "owner")
  private Set<DocumentCategory> documentCategories;

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "owner")
  private Set<DocumentLabel> documentLabels;

  @OneToMany
  @JoinColumn
  private List<Product> products;

  @OneToOne
  @JoinColumn
  private BusinessAccountType accountType;
  
  @Column
  private String partnerBannerLocation;
  
  @Column(length=1000)
  private String partnerBannerText;
  
  @Column(length=1000)
  private String partnerWelcomeMessage;
  
  @Column
  private String partnerBannerTextColor;
  
  @Column
  private Boolean showPartners = Boolean.TRUE;
  
  public OrganizationType getOrganizationType() {
    return organizationType;
  }

  public void setOrganizationType(OrganizationType organizationType) {
    this.organizationType = organizationType;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public AssociateShowOptionType getAssociateShowPublicOpt() {
    return associateShowPublicOpt;
  }

  public void setAssociateShowPublicOpt(
      AssociateShowOptionType associateShowPublicOpt) {
    this.associateShowPublicOpt = associateShowPublicOpt;
  }

  public AssociateShowOptionType getAssociateShowCommunityOpt() {
    return associateShowCommunityOpt;
  }

  public void setAssociateShowCommunityOpt(
      AssociateShowOptionType associateShowCommunityOpt) {
    this.associateShowCommunityOpt = associateShowCommunityOpt;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getBusinessPhoneNumber() {
    return businessPhoneNumber;
  }

  public void setBusinessPhoneNumber(String businessPhoneNumber) {
    this.businessPhoneNumber = businessPhoneNumber;
  }

  public String getBusinessTaxId() {
    return businessTaxId;
  }

  public void setBusinessTaxId(String businessTaxId) {
    this.businessTaxId = businessTaxId;
  }

  public String getBusinessStateTaxId() {
    return businessStateTaxId;
  }

  public void setBusinessStateTaxId(String businessStateTaxId) {
    this.businessStateTaxId = businessStateTaxId;
  }

  public String getPrimaryEmail() {
    return primaryEmail;
  }

  public void setPrimaryEmail(String primaryEmail) {
    this.primaryEmail = primaryEmail;
  }

  public OrganizationLocationShowPreferenceType getLocationPreferenceType() {
    return locationPreferenceType;
  }

  public void setLocationPreferenceType(
      OrganizationLocationShowPreferenceType locationPreferenceType) {
    this.locationPreferenceType = locationPreferenceType;
  }

  public OrganizationLocationGroupingPrefType getLocationGrpPrefType() {
    return locationGrpPrefType;
  }

  public void setLocationGrpPrefType(
      OrganizationLocationGroupingPrefType locationGrpPrefType) {
    this.locationGrpPrefType = locationGrpPrefType;
  }

  public List<OrganizationSearchPreference> getSearchPreferences() {
    return searchPreferences;
  }

  public void setSearchPreferences(
      List<OrganizationSearchPreference> searchPreferences) {
    this.searchPreferences = searchPreferences;
  }

  public PhotoViewOption getLogoViewOptionType() {
    return logoViewOptionType;
  }

  public void setLogoViewOptionType(PhotoViewOption logoViewOptionType) {
    this.logoViewOptionType = logoViewOptionType;
  }

  public String getBusinessHome() {
    return businessHome;
  }

  public void setBusinessHome(String businessHome) {
    this.businessHome = businessHome;
  }

  public String getBusinessDescription() {
    return businessDescription;
  }

  public void setBusinessDescription(String businessDescription) {
    this.businessDescription = businessDescription;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getCommunityLandingPageUrl() {
    return communityLandingPageUrl;
  }

  public void setCommunityLandingPageUrl(String communityLandingPageUrl) {
    this.communityLandingPageUrl = communityLandingPageUrl;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public OrganizationStatus getStatus() {
    return status;
  }

  public void setStatus(OrganizationStatus status) {
    this.status = status;
  }

  public String getProductPageUrl() {
    return productPageUrl;
  }

  public void setProductPageUrl(String productPageUrl) {
    this.productPageUrl = productPageUrl;
  }

  public Set<DocumentCategory> getDocumentCategories() {
    return documentCategories;
  }

  public void setDocumentCategories(Set<DocumentCategory> documentCategories) {
    this.documentCategories = documentCategories;
  }

  public Set<DocumentLabel> getDocumentLabels() {
    return documentLabels;
  }

  public void setDocumentLabels(Set<DocumentLabel> documentLabels) {
    this.documentLabels = documentLabels;
  }

  @Override
  public String getImage() {
    return getLogoUrl();
  }

  @Override
  public String getDisplayName() {
    return StringUtils.isBlank(businessName) ? getName() : businessName;
  }
}
