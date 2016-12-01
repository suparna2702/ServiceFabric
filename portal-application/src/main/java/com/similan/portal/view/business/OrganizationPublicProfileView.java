package com.similan.portal.view.business;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.activity.CommentDetail;
import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.entity.lead.ContactMessageType;
import com.similan.framework.dto.OrganizationAddressDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.review.ReviewCommentDto;
import com.similan.framework.dto.review.ReviewCommentSummeryDto;
import com.similan.portal.model.ProductDetailModel;
import com.similan.portal.service.security.SecurityUtils;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.product.dto.basic.ProductDto;
import com.similan.service.api.product.dto.basic.ProductFeatureDto;
import com.similan.service.api.product.dto.basic.ProductTagDto;

@Scope("view")
@Component("orgPublicView")
@Slf4j
public class OrganizationPublicProfileView extends BaseView {

  private static final long serialVersionUID = 1L;

  private OrganizationDetailInfoDto orgInfo;

  private OrganizationDetailInfoDto loggedInOrg;

  private MemberInfoDto member;

  private boolean sameLoggedInOrg = true;

  private CommentDetail commentShare = new CommentDetail();

  private Marker selectMarker;

  private MapModel simpleModel;

  private ReviewCommentSummeryDto reviewSummery = new ReviewCommentSummeryDto();

  private CartesianChartModel reviewSummaryChart;

  private ContactMessageDetail message = new ContactMessageDetail();

  /* US latitude longitude by default 37.702387, 238.406370 (Dublin) */
  private Double mapCenterLat = 37.702387;

  private Double mapCenterLong = 238.406370;

  private String productRequestComment;

  protected List<ProductDetailModel> productList = null;

  private List<PartnerProgramDefinitionDto> partnerPrograms = null;

  private List<NewsDto> newsItems = null;

  private List<MemberInfoDto> teamMembers = null;

  @PostConstruct
  public void init() {

    String orgId = this.getContextParam("oid");
    String partnerId = this.getContextParam("pid");

    log.info("Getting org public profile with id fetched from param "
        + orgId);

    try {
      member = this.getMemberService().getMemberById(
          SecurityUtils.getRequiredCurrentUserId());

      OrganizationBasicInfoDto employer = member.getEmployer();
      if (employer != null) {
        loggedInOrg = this.orgService.viewBusinessProfile(employer.getId(),
            this.getMemberKey(member));
        log.info("Logged In org ID = " + loggedInOrg.getId());
      }

    } catch (Exception exp) {
      // Exception if member not logged in. Not an error.
      log.info("No member logged in", exp);
    }

    try {

      if (orgId != null) {
        log.info("OrgId is not null");
        /* it can come from search page */
        Long id = Long.valueOf(orgId);
        if (loggedInOrg != null) {
          if (loggedInOrg.getId() != id) {
            this.sameLoggedInOrg = false;
          }
        }
        orgInfo = this.getOrgService().viewBusinessProfile(Long.valueOf(orgId),
            this.getMemberKey(member));

      } else {
        log.info("OrgId is null");

        // the page is navigated from menu so no id
        // we take the logged in org id

        orgInfo = loggedInOrg;
      }

      // regardless we fetch org-info from DB
      // this is the case of click through
      // if(this.sameLoggedInOrg != true){
      // if (member != null)
      // this.memberService.createClickThroughLeadForOrganization(
      // member, orgInfo);
      // }
    } catch (Exception exp) {
      log.info("Error in fetching or with id " + orgId, exp);
    }

    /* also generate map markers */

    List<OrganizationAddressDto> addressList = orgInfo.getLocations();
    for (OrganizationAddressDto addr : addressList) {
      this.addMapOrgMarker(addr);
    }

    this.generateMapCenter();

    /* get selected partner if needed */
    if (partnerId != null && orgInfo != null) {

      Long idPartner = Long.valueOf(partnerId);
      orgInfo.setSelectedPartnerProgram(this.orgService
          .getPartnerProgramById(idPartner));
    }

    initContacts();

    /* get the partner programs */
    this.partnerPrograms = this.getPartnerManagementService()
        .getPartnerProgramsForBusiness(orgInfo);

    /* get news feeds */
    this.newsItems = this.getNewsService().getLatest(this.getOrgKey(orgInfo),
        this.getMemberKey(member));

    /* get team members */
    this.teamMembers = this.getOrgService().getTeamMembers(orgInfo.getId());

    /* get the products */
    SocialActorKey orgKey = this.getOrgKey(orgInfo);
    List<ProductDto> products = this.getProductService().getForOwner(orgKey);
    if (products != null) {
      this.productList = new ArrayList<ProductDetailModel>();
      for (ProductDto prod : products) {
        List<ProductTagDto> tags = this.getProductService().getTags(
            prod.getKey());
        List<ProductFeatureDto> features = this.getProductService()
            .getFeatures(prod.getKey());

        ProductDetailModel productModel = new ProductDetailModel();
        productModel.setProduct(prod);
        productModel.setTags(tags);
        productModel.setFeatures(features);

        this.productList.add(productModel);
      }
    }
  }

  public List<NewsDto> getNewsItems() {
    return newsItems;
  }

  public void setNewsItems(List<NewsDto> newsItems) {
    this.newsItems = newsItems;
  }

  public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
    return partnerPrograms;
  }

  public List<ProductDetailModel> getProductList() {
    return productList;
  }

  public void setProductList(List<ProductDetailModel> productList) {
    this.productList = productList;
  }

  public ContactMessageDetail getMessage() {
    return message;
  }

  public void setMessage(ContactMessageDetail message) {
    this.message = message;
  }

  public CartesianChartModel getReviewSummaryChart() {
    return reviewSummaryChart;
  }

  public void setReviewSummaryChart(CartesianChartModel reviewSummaryChart) {
    this.reviewSummaryChart = reviewSummaryChart;
  }

  public ReviewCommentSummeryDto getReviewSummery() {
    return reviewSummery;
  }

  public void setReviewSummery(ReviewCommentSummeryDto reviewSummery) {
    this.reviewSummery = reviewSummery;
  }

  public Double getMapCenterLat() {
    return mapCenterLat;
  }

  public void setMapCenterLat(Double mapCenterLat) {
    this.mapCenterLat = mapCenterLat;
  }

  public Double getMapCenterLong() {
    return mapCenterLong;
  }

  public void setMapCenterLong(Double mapCenterLong) {
    this.mapCenterLong = mapCenterLong;
  }

  private void initContacts() {
    /*try {
      contacts = new ArrayList<Contact>();
      if (member != null) {
        if (this.member.getContacts() == null) {
          contacts.addAll(this.memberService.getContacts(member));
        } else {
          contacts.addAll(this.member.getContacts());
        }
      }
    } catch (Exception exp) {
      log.info("Error in fetching contacts.", exp);
    }*/
  }

  public MapModel getSimpleModel() {
    return simpleModel;
  }

  public void setSimpleModel(MapModel simpleModel) {
    this.simpleModel = simpleModel;
  }

  public Marker getSelectMarker() {
    return selectMarker;
  }

  public void setSelectMarker(Marker selectMarker) {
    this.selectMarker = selectMarker;
  }

  /**
   * This method gets called when user selects a marker in the MAP
   * 
   * @param event
   */
  public void onMarkerSelect(OverlaySelectEvent event) {
    this.selectMarker = (Marker) event.getOverlay();
  }

   public void addMapOrgMarker(OrganizationAddressDto address) {

    String markerTag = "Business Address";

    if (orgInfo.getBusinessName() != null) {
      markerTag = orgInfo.getBusinessName();
    }

    if (simpleModel == null) {
      simpleModel = new DefaultMapModel();
    }

    Double latitude = address.getLatitude();
    Double longitude = address.getLongitude();

    if ((latitude != null) && (longitude != null)) {

      if ((latitude != Double.MIN_VALUE) && (longitude != Double.MIN_VALUE)) {

        log.info("Latitude " + latitude + " longitude " + longitude);
        LatLng coordOrg = new LatLng(latitude, longitude);
        Marker mapMarker = new Marker(coordOrg, markerTag, address);

        simpleModel.addOverlay(mapMarker);
      }
    }
  }

  public void generateMapCenter() {

    if (this.simpleModel != null) {
      List<Marker> markerList = this.simpleModel.getMarkers();

      if ((markerList != null) && (markerList.size() > 0)) {

        Marker center = markerList.get(0);
        this.mapCenterLat = center.getLatlng().getLat();
        this.mapCenterLong = center.getLatlng().getLng();
        log.info("Map center Latitude " + this.mapCenterLat + " longitude "
            + this.mapCenterLong);
      }
    }
  }

  /**
   * for searching resellers. This is to navigate to that page
   */
  public void resellerSearch() {

    log.info("Navigating to reseller search page");
    facesHelper.redirect("/search/resellerSearch.xhtml?oid="
        + this.orgInfo.getId());
  }

  public CommentDetail getCommentShare() {
    return commentShare;
  }

  public void setCommentShare(CommentDetail commentShare) {
    this.commentShare = commentShare;
  }

  public boolean isSameLoggedInOrg() {
    return sameLoggedInOrg;
  }

  public void setSameLoggedInOrg(boolean sameLoggedInOrg) {
    this.sameLoggedInOrg = sameLoggedInOrg;
  }

  public void participatePartnerProgram() {
    log.info("Participate Partner program");

    try {

      if (this.orgInfo.getSelectedPartnerProgram() != null) {

        long programId = this.orgInfo.getSelectedPartnerProgram().getId();
        log.info("Participate Partner program " + programId);

        if (loggedInOrg != null) {
          if (loggedInOrg.getId() != this.orgInfo.getSelectedPartnerProgram()
              .getOwner().getId()) {

            boolean isParticipating = loggedInOrg
                .isParticipatingInPartnerProgram(this.orgInfo
                    .getSelectedPartnerProgram().getId());

            if (isParticipating == false) {

              log.info("Participating org " + loggedInOrg.getId()
                  + " member " + member.getId() + " Program "
                  + this.orgInfo.getSelectedPartnerProgram().getId());
              orgService.initiatePartnerParticipation(loggedInOrg, member,
                  this.orgInfo.getSelectedPartnerProgram());

              /* add so that GUI is refreshed */
              loggedInOrg.getParticipatingPartnerPrograms().add(
                  this.orgInfo.getSelectedPartnerProgram());

              FacesContext
                  .getCurrentInstance()
                  .addMessage(
                      null,
                      new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
                          "Partner participation request successfully processed. Welcome."));

            } else {
              FacesContext.getCurrentInstance().addMessage(
                  null,
                  new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
                      "You are already participating in this program"));
            }

          } else {

            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
                    "You cannot participate in your own program"));

          }

        } else {
          FacesContext
              .getCurrentInstance()
              .addMessage(
                  null,
                  new FacesMessage(
                      FacesMessage.SEVERITY_WARN,
                      "Message",
                      "You cannot participate in partner program since you are not associated with any business"));

        }
      } else {
        log.info("No partner program selected");
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
                "Please select a partner program"));
      }
    } catch (Exception exp) {

      log.error("Error in partner participation ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message",
              "Error in partner participation"));

    }
  }

  public List<MemberInfoDto> getTeamMembers() {
    return this.teamMembers;
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
    this.orgInfo = orgInfo;
  }

  public String getPublicLogo() {
    return getOrgInfo().getLogoLocation();
  }

  public String getProductRequestComment() {
    return productRequestComment;
  }

  public void setProductRequestComment(String productRequestComment) {
    this.productRequestComment = productRequestComment;
  }

  public void newsViewed(Long id) {
    SocialActorKey viewer = this.getMemberKey(member);
    log.info("Viewer " + viewer + " item " + id);

    this.getNewsService().newsViewed(id, viewer);
  }

  /**
   * Launches the view screen
   */
  public void viewPartnerProgram() {

    log.info("View Partner program");

    if (this.orgInfo.getSelectedPartnerProgram() != null) {

      long programId = this.orgInfo.getSelectedPartnerProgram().getId();
      log.info("View Partner program " + programId);
      StringBuilder partnerViewUrl = new StringBuilder();
      partnerViewUrl.append("/partner/partnerProgramView.xhtml?pid=")
          .append(programId).append("&oid=").append(this.orgInfo.getId());
      facesHelper.redirect(partnerViewUrl.toString());
    } else {
      log.info("No partner program selected");
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message",
              "Please select a partner program"));

    }

  }

  public void reviewShare(ReviewCommentDto review) {
    log.info("Share for comment " + review.getReviewId());

  }

  public void reviewFlag(ReviewCommentDto review) {
    log.info("Flag for comment " + review.getReviewId());

    CommentDetail commentInfo = review.getCommentInfo();
    commentInfo.setFlagCount((commentInfo.getFlagCount() + 1));

    /* now update the actual data base */

  }

  public void share() {
    /*if (selectedContacts.length == 0)
      return;
    *//**
     * 1. Check whether sharing itself 2. Otherwise move on with follow and show
     * thanks
     *//*
    if (this.sameLoggedInOrg == true) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
              "Cannot share with yourself "));
      return;
    }

    log.info("Sharing business " + this.getOrgInfo().getFullName());

    this.orgService.share(this.member, this.orgInfo, selectedContacts);
    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
            "Thanks for sharing us "));*/
  }

  public void follow() {
    /**
     * 1. Check whether following itself 2. Otherwise move on with follow and
     * show thanks
     */
    if (this.sameLoggedInOrg == true) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
              "Cannot follow yourself "));
      return;
    }

    log.info("Following business " + this.getOrgInfo().getFullName());
    this.orgService.follow(this.member, this.orgInfo);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
            "You are now following " + this.getOrgInfo().getFullName()));
  }

  public void sendMessage() {
    log.info("Sending message " + this.message);
    try {

      if (this.sameLoggedInOrg == true) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
                "Cannot send message yourself "));
        return;
      }

      if (this.orgInfo == null) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure",
                "Cannot send a message since receiver is not valid "));
        return;
      }

      log.info("The message to be sent " + this.message.getMessage()
          + " to business " + this.orgInfo.getId());

      if (StringUtils.isEmpty(this.orgInfo.getEmail())) {
        FacesContext
            .getCurrentInstance()
            .addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure",
                    "Cannot send a message since receiver does not have a configured email "));
        return;
      }

      this.message.setContactMessageType(ContactMessageType.EmailMessage);
      this.memberService.sendContactMessageToOrganization(message, this.member,
          this.orgInfo);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Thanks. Your message has been successfully sent"));
    } catch (Exception exp) {

      log.error("Error in sending message ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Error in sending message " + exp.getMessage()));
    }

  }

  public void requestProduct() {
    this.orgService.requestProduct(productRequestComment, member, orgInfo);
    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Thanks. Your message has been successfully sent"));
    productRequestComment = null;
  }

  public boolean isMemberFollowingOrg() {
    if (member == null)
      return false;
    return this.orgService.isMemberFollowingOrg(member, orgInfo);
  }

  public String getTotalPartners() {
    if (this.orgInfo.getParticipatingPartnerPrograms() != null) {
      return String.valueOf(this.orgInfo.getParticipatingPartnerPrograms()
          .size());
    } else {
      return String.valueOf(0);
    }
  }
}
