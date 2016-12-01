package com.similan.framework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.DualListModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.similan.domain.entity.community.AssociateShowOptionType;
import com.similan.domain.entity.community.OrganizationSearchPreferenceType;
import com.similan.domain.entity.community.OrganizationStatus;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;

@Slf4j
public class OrganizationDetailInfoDto implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String IMAGES_DEFAULT_PARTNER_BANNER = "/images/bannerDefault.jpg";

  private Long id;

  private String name;

  private Integer newCommentRating;

  private String businessName;

  private String businessPhone;

  private String businessTaxId;

  private String businessStateTaxId;

  private String businessPrimaryEmail;

  private String homePageUrl;

  private String landingPageUrl;

  private String businessDescription = null;

  private List<MemberInfoDto> teamMembers = new ArrayList<MemberInfoDto>();

  private List<MemberInfoDto> associates = new ArrayList<MemberInfoDto>();

  private List<OrganizationAddressDto> locations = new ArrayList<OrganizationAddressDto>();

  private List<PartnerProgramDefinitionDto> partnerPrograms = new ArrayList<PartnerProgramDefinitionDto>();

  private List<PartnerProgramDefinitionDto> participatingPartnerPrograms = new ArrayList<PartnerProgramDefinitionDto>();

  private PartnerProgramDefinitionDto selectedPartnerProgram = null;

  private OrganizationAddressDto selectedLocation = new OrganizationAddressDto();

  private OrganizationAddressDto primarySearchVisibleLocation = null;

  private List<ServiceInfo> serviceInfoList = new ArrayList<ServiceInfo>();

  private List<String> selectedOptions;

  private String locationSettings;

  private String locationGroupSettings;

  private String industry;

  private DualListModel<OrganizationDetailInfoDto> distributors = null;

  private PhotoViewOption logoViewOptionType = PhotoViewOption.HidePhoto;

  private String logoLocation = null;

  private AssociateShowOptionType associateShowPublicOpt = AssociateShowOptionType.All;

  private AssociateShowOptionType associateShowCommunityOpt = AssociateShowOptionType.All;

  private OrganizationType businessType = OrganizationType.UNSPECIFIED;

  private SocialMemberVisibilityType memberVisibilityType;

  private MapModel mapModel = null;

  private String embeddedIdentity = null;

  private OrganizationStatus status = null;

  private List<OrganizationAddressDto> deletedAddressList = new ArrayList<OrganizationAddressDto>();

  private String contactCategory = com.similan.service.api.connection.dto.basic.ContactType.Other
      .toString();

  private String productPageUrl;

  private String partnerBannerLocation;

  private String partnerBannerText;

  private String partnerWelcomeMessage;

  private String partnerBannerTextColor;

  private Boolean showPartners = Boolean.TRUE;

  public OrganizationDetailInfoDto() {

    businessType = OrganizationType.UNSPECIFIED;
    memberVisibilityType = SocialMemberVisibilityType.VisiblePublic;
    associateShowPublicOpt = AssociateShowOptionType.All;
    associateShowCommunityOpt = AssociateShowOptionType.All;
    logoViewOptionType = PhotoViewOption.HidePhoto;
    OrganizationSearchPreferenceType[] orgSearchPreferences = OrganizationSearchPreferenceType
        .values();
    selectedOptions = new ArrayList<String>();

    for (OrganizationSearchPreferenceType orgSearchPrefType : orgSearchPreferences) {
      selectedOptions.add(orgSearchPrefType.toString());
    }

    showPartners = Boolean.TRUE;
  }

  public boolean isParticipatingInPartnerProgram(Long programId) {
    boolean retValue = false;

    for (PartnerProgramDefinitionDto partnership : this.participatingPartnerPrograms) {
      if (partnership.getId() == programId) {
        retValue = true;
      }
    }

    return retValue;
  }

  public MapModel getMapModel() {

    if (mapModel == null) {
      mapModel = new DefaultMapModel();

      for (OrganizationAddressDto location : this.locations) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        if ((latitude != null) && (longitude != null)) {

          if ((latitude != Double.MIN_VALUE) && (longitude != Double.MIN_VALUE)) {

            log.info("Latitude " + latitude + " longitude " + longitude);
            LatLng coordOrg = new LatLng(latitude, longitude);
            mapModel.addOverlay(new Marker(coordOrg, this.businessName));
          }
        }
      }
    }

    return mapModel;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getShowPartners() {
    return showPartners;
  }

  public void setShowPartners(Boolean showPartners) {
    if (showPartners != null) {
      this.showPartners = showPartners;
    }

  }

  /**
   * This methos evaluates org role used in UI for filtering widgets
   * 
   * @param type
   * @return
   */
  public boolean isOrganizationType(String type) {
    boolean ret = false;
    OrganizationType roleToEvaluate = OrganizationType.valueOf(type);

    if (roleToEvaluate.equals(this.businessType) == true) {
      ret = true;
    }

    log.info("Type to be evaluated " + type + " actual role "
        + this.businessType + " return value " + ret);
    return ret;
  }

  public String getPartnerBannerTextColor() {
    return partnerBannerTextColor;
  }

  public void setPartnerBannerTextColor(String partnerBannerTextColor) {
    this.partnerBannerTextColor = partnerBannerTextColor;
  }

  public String getPartnerWelcomeMessage() {
    return partnerWelcomeMessage;
  }

  public void setPartnerWelcomeMessage(String partnerWelcomeMessage) {
    this.partnerWelcomeMessage = partnerWelcomeMessage;
  }

  public String getPartnerBannerLocation() {
    return partnerBannerLocation;
  }

  public void setPartnerBannerLocation(String partnerBannerLocation) {
    this.partnerBannerLocation = partnerBannerLocation;
  }

  public String getPartnerBannerText() {
    if (StringUtils.isBlank(partnerBannerText)) {
      return this.businessDescription;
    }

    return partnerBannerText;
  }

  public void setPartnerBannerText(String partnerBannerText) {
    this.partnerBannerText = partnerBannerText;
  }

  public List<OrganizationAddressDto> getDeletedAddressList() {
    return deletedAddressList;
  }

  public void setDeletedAddressList(
      List<OrganizationAddressDto> deletedAddressList) {
    this.deletedAddressList = deletedAddressList;
  }

  public void setMapModel(MapModel mapModel) {
    this.mapModel = mapModel;
  }

  public OrganizationAddressDto getPrimarySearchVisibleLocation() {
    return primarySearchVisibleLocation;
  }

  public void setPrimarySearchVisibleLocation(
      OrganizationAddressDto primarySearchVisibleLocation) {
    this.primarySearchVisibleLocation = primarySearchVisibleLocation;
  }

  public SocialMemberVisibilityType getMemberVisibilityType() {
    return memberVisibilityType;
  }

  public void setMemberVisibilityType(
      SocialMemberVisibilityType memberVisibilityType) {
    this.memberVisibilityType = memberVisibilityType;
  }

  public List<PartnerProgramDefinitionDto> getParticipatingPartnerPrograms() {
    return participatingPartnerPrograms;
  }

  public void setParticipatingPartnerPrograms(
      List<PartnerProgramDefinitionDto> participatingPartnerPrograms) {
    this.participatingPartnerPrograms = participatingPartnerPrograms;
  }

  public PartnerProgramDefinitionDto getSelectedPartnerProgram() {
    return selectedPartnerProgram;
  }

  public void setSelectedPartnerProgram(
      PartnerProgramDefinitionDto selectedPartnerProgram) {
    this.selectedPartnerProgram = selectedPartnerProgram;
  }

  public DualListModel<OrganizationDetailInfoDto> getDistributors() {

    if (distributors == null) {
      List<OrganizationDetailInfoDto> srcDistributors = new ArrayList<OrganizationDetailInfoDto>();
      OrganizationDetailInfoDto dist1 = new OrganizationDetailInfoDto();
      dist1.setBusinessName("Next USA");
      srcDistributors.add(dist1);

      OrganizationDetailInfoDto dist2 = new OrganizationDetailInfoDto();
      dist2.setBusinessName("Scan Source");
      srcDistributors.add(dist2);

      OrganizationDetailInfoDto dist3 = new OrganizationDetailInfoDto();
      dist3.setBusinessName("Ingram Micro");
      srcDistributors.add(dist3);

      List<OrganizationDetailInfoDto> targetDistributors = new ArrayList<OrganizationDetailInfoDto>();

      distributors = new DualListModel<OrganizationDetailInfoDto>(
          srcDistributors, targetDistributors);

    }

    return distributors;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public void setBusinessType(OrganizationType businessType) {
    this.businessType = businessType;
  }

  public OrganizationType getBusinessType() {
    return businessType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBusinessPhone() {
    return businessPhone;
  }

  public void setBusinessPhone(String businessPhone) {
    this.businessPhone = businessPhone;
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

  public String getBusinessPrimaryEmail() {
    return businessPrimaryEmail;
  }

  public void setBusinessPrimaryEmail(String businessPrimaryEmail) {
    this.businessPrimaryEmail = businessPrimaryEmail;
  }

  public String getAssociateShowPublicOptType() {
    if (associateShowPublicOpt == null) {
      associateShowPublicOpt = AssociateShowOptionType.All;
    }
    return associateShowPublicOpt.toString();
  }

  public void setAssociateShowPublicOptType(String optType) {
    associateShowPublicOpt = AssociateShowOptionType.valueOf(optType);
  }

  public AssociateShowOptionType getAssociateShowPublicOpt() {
    return associateShowPublicOpt;
  }

  public void setAssociateShowPublicOpt(
      AssociateShowOptionType associateShowPublicOpt) {
    this.associateShowPublicOpt = associateShowPublicOpt;
  }

  public void setAssociateShowPublicOpt(String option) {
    this.associateShowPublicOpt = AssociateShowOptionType.valueOf(option);
  }

  public String getAssociateShowCommunityOptType() {

    if (associateShowCommunityOpt == null) {
      associateShowCommunityOpt = AssociateShowOptionType.All;
    }

    return associateShowCommunityOpt.toString();
  }

  public void setAssociateShowCommunityOptType(String optType) {
    associateShowCommunityOpt = AssociateShowOptionType.valueOf(optType);
  }

  public void setAssociateShowCommunityOpt(String associateShowcommunityOpt) {
    this.associateShowCommunityOpt = AssociateShowOptionType
        .valueOf(associateShowcommunityOpt);
  }

  public AssociateShowOptionType getAssociateShowCommunityOpt() {
    return associateShowCommunityOpt;
  }

  public void setAssociateShowCommunityOpt(
      AssociateShowOptionType associateShowcommunityOpt) {
    this.associateShowCommunityOpt = associateShowcommunityOpt;
  }

  public String getLogoLocation() {
    return this.logoViewOptionType.effectivePhoto("/images/businessLogo.jpg",
        this.logoLocation);
  }

  public void setLogoLocation(String logoLocation) {
    this.logoLocation = logoLocation;
  }

  public PhotoViewOption getLogoViewOptionType() {
    return logoViewOptionType;
  }

  public void setLogoViewOptionType(PhotoViewOption logoViewOptionType) {
    this.logoViewOptionType = logoViewOptionType;
  }

  public String getPhotoViewOption() {
    return logoViewOptionType.toString();
  }

  public void setPhotoViewOption(String photoViewOption) {
    this.logoViewOptionType = PhotoViewOption.valueOf(photoViewOption);
  }

  public void setDistributors(
      DualListModel<OrganizationDetailInfoDto> distributors) {
    this.distributors = distributors;
  }

  public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
    return partnerPrograms;
  }

  public void setPartnerPrograms(
      List<PartnerProgramDefinitionDto> partnerPrograms) {
    this.partnerPrograms = partnerPrograms;
  }

  public Integer getNewCommentRating() {
    return newCommentRating;
  }

  public void setNewCommentRating(Integer newCommentRating) {
    this.newCommentRating = newCommentRating;
  }

  public OrganizationAddressDto getSelectedLocation() {
    return selectedLocation;
  }

  public void setSelectedLocation(OrganizationAddressDto selectedLocation) {
    log.info("Selected location " + selectedLocation.toString());
    this.selectedLocation = selectedLocation;
  }

  /**
   * removes location from the list
   * 
   * @param selectedLocation
   */
  public void removeLocation(OrganizationAddressDto location) {
    log.info("Removing location " + location.toString());
    OrganizationAddressDto objectTobeRemoved = null;

    for (OrganizationAddressDto addr : this.locations) {
      if (addr.getId() == location.getId()) {
        log.info("Remove business address " + addr.getId());
        objectTobeRemoved = addr;
        this.deletedAddressList.add(addr);
        break;
      }
    }

    /* now do the actual removal */
    if (objectTobeRemoved != null) {
      this.locations.remove(objectTobeRemoved);
    }
  }

  public void editLocation() {
    log.info("Updating location " + selectedLocation.toString());

    for (OrganizationAddressDto addr : this.locations) {
      if (addr.getId() == selectedLocation.getId()) {
        log.info("found business address " + addr.getId());
        addr.updateAddress(selectedLocation);
      }
    }
  }

  public String getLandingPageUrl() {
    return landingPageUrl;
  }

  public void setLandingPageUrl(String landingPageUrl) {
    this.landingPageUrl = landingPageUrl;
  }

  public String getLocationGroupSettings() {
    return locationGroupSettings;
  }

  public void setLocationGroupSettings(String locationGroupSettings) {
    this.locationGroupSettings = locationGroupSettings;
  }

  public String getLocationSettings() {
    return locationSettings;
  }

  public void setLocationSettings(String locationSettings) {
    this.locationSettings = locationSettings;
  }

  public List<String> getSelectedOptions() {
    return selectedOptions;
  }

  public void setSelectedOptions(List<String> selectedOptions) {
    this.selectedOptions = selectedOptions;
  }

  public List<MemberInfoDto> getAssociates() {

    if (this.associates.size() <= 1) {

      for (int i = 0; i < 6; i++) {
        MemberInfoDto info = new MemberInfoDto();
        info.setFirstName("Suparna");
        info.setLastName("Pal");
        info.setRelationshipType("Sales");
        info.setDescription("A good employee who has dedicated his heart for the growth of the company");
        this.associates.add(info);
      }
    }

    return associates;
  }

  public void setAssociates(List<MemberInfoDto> associates) {
    this.associates = associates;
  }

  public List<ServiceInfo> getServiceInfoList() {

    if (this.serviceInfoList.size() <= 0) {
      ServiceInfo info1 = new ServiceInfo();
      info1.setServiceType("Cabling System");
      info1.setServiceDescription("Test description");
      this.serviceInfoList.add(info1);

      ServiceInfo info2 = new ServiceInfo();
      info2.setServiceType("Custom Software Development");
      info2.setServiceDescription("Test description");
      this.serviceInfoList.add(info2);
    }

    return serviceInfoList;
  }

  public void setServiceInfoList(List<ServiceInfo> serviceInfoList) {
    this.serviceInfoList = serviceInfoList;
  }

  public void setLocations(List<OrganizationAddressDto> locations) {
    this.locations = locations;
  }

  public List<OrganizationAddressDto> getLocations() {
    if (locations == null) {
      locations = new ArrayList<OrganizationAddressDto>();
    }

    return locations;
  }

  public String getBusinessDescription() {

    if (this.businessDescription == null) {
      businessDescription = "";
    }

    return businessDescription;
  }

  public void setBusinessDescription(String businessDescription) {
    this.businessDescription = businessDescription;
  }

  public List<MemberInfoDto> getTeamMembers() {

    return teamMembers;
  }

  public void setTeamMembers(List<MemberInfoDto> teamMembers) {
    this.teamMembers = teamMembers;
  }

  public String getBusinessName() {
    return businessName;
  }

  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }

  public String getHomePageUrl() {
    return homePageUrl;
  }

  public void setHomePageUrl(String homePageUrl) {
    this.homePageUrl = homePageUrl;
  }

  public String getImagePath() {
    return this.getLogoLocation();
  }

  public String getFullName() {
    return this.getBusinessName();
  }

  public String getTitle() {
    return this.getBusinessName();
  }

  public String getEmail() {
    return this.businessPrimaryEmail;
  }

  public String getAddress() {
    String addr = "";

    if ((this.getLocations() != null) && (this.getLocations().size() > 0)) {
      OrganizationAddressDto location = this.getLocations().get(0);
      addr = location.getCity() + " / " + location.getCountry();
    }
    return addr;
  }

  public String getLabel() {
    return this.getBusinessName();
  }

  public String getDescription() {
    return this.getBusinessDescription();
  }

  public String getEmbeddedIdentity() {
    return embeddedIdentity;
  }

  public void setEmbeddedIdentity(String embeddedIdentity) {
    this.embeddedIdentity = embeddedIdentity;
  }

  public OrganizationStatus getStatus() {
    return status;
  }

  public void setStatus(OrganizationStatus status) {
    this.status = status;
  }

  public void setIsBusiness(boolean value) {

  }

  public void setIsMember(boolean value) {

  }

  public boolean getIsBusiness() {
    return true;
  }

  public boolean getIsMember() {
    return false;
  }

  public void setCompany(String company) {

  }

  public String getCompany() {
    return this.getBusinessName();
  }

  public String getStreet() {

    if ((this.getLocations() != null) && (this.getLocations().size() > 0)) {
      OrganizationAddressDto location = this.getLocations().get(0);
      return location.getStreet();
    }

    return null;
  }

  public String getCity() {

    if ((this.getLocations() != null) && (this.getLocations().size() > 0)) {
      OrganizationAddressDto location = this.getLocations().get(0);
      return location.getCity();
    }

    return null;
  }

  public String getState() {
    if ((this.getLocations() != null) && (this.getLocations().size() > 0)) {
      OrganizationAddressDto location = this.getLocations().get(0);
      return location.getState();
    }

    return null;
  }

  public String getCountry() {
    if ((this.getLocations() != null) && (this.getLocations().size() > 0)) {
      OrganizationAddressDto location = this.getLocations().get(0);
      return location.getCountry();
    }

    return null;
  }

  public String getZipCode() {
    if ((this.getLocations() != null) && (this.getLocations().size() > 0)) {
      OrganizationAddressDto location = this.getLocations().get(0);
      return location.getZipCode();
    }

    return null;
  }

  public void getContactCategory(String contactCat) {
    this.contactCategory = contactCat;
  }

  public void setContactCategory(String category) {
    this.contactCategory = category;
  }

  public String getContactCategory() {
    return this.contactCategory;
  }

  public Long getCompanyId() {
    return this.id;
  }

  public String getProductPageUrl() {
    return productPageUrl;
  }

  public void setProductPageUrl(String productPageUrl) {
    this.productPageUrl = productPageUrl;
  }

}