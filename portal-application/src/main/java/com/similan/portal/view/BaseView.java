package com.similan.portal.view;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.crocodoc.CrocodocConstants;
import com.crocodoc.CrocodocException;
import com.crocodoc.CrocodocSession;
import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.metadata.StateType;
import com.similan.framework.box.BoxClient;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.databean.CountryBean;
import com.similan.portal.model.ContactSource;
import com.similan.portal.model.DocumentInstanceAndDocumentModel;
import com.similan.portal.model.SelectableContact;
import com.similan.portal.service.AffiliateLeadService;
import com.similan.portal.service.CampaignService;
import com.similan.portal.service.DomainEventService;
import com.similan.portal.service.MemberService;
import com.similan.portal.service.OrganizationService;
import com.similan.portal.service.PlatformMetadataService;
import com.similan.portal.service.SearchService;
import com.similan.portal.service.SocialService;
import com.similan.portal.view.handler.ImageUploadHandler;
import com.similan.service.api.BusinessProcessManagementService;
import com.similan.service.api.CampaignManagementService;
import com.similan.service.api.NewsService;
import com.similan.service.api.PartnerManagementService;
import com.similan.service.api.advertisement.DisplayNoticeService;
import com.similan.service.api.audit.AuditEventService;
import com.similan.service.api.bookmark.BookmarkService;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceService;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceSharedDocumentService;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceTaskService;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.comment.CommentService;
import com.similan.service.api.community.EmployeeRoleAdminService;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.connection.ConnectionService;
import com.similan.service.api.document.DocumentCategoryService;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.DocumentLabelService;
import com.similan.service.api.document.DocumentService;
import com.similan.service.api.document.dto.basic.DocumentViewerItemType;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentViewerItemDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.feed.FeedService;
import com.similan.service.api.feed.dto.basic.FeedEntryType;
import com.similan.service.api.managementworkspace.ManagementWorkspaceService;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.product.ProductService;
import com.similan.service.api.settings.NotificationConfigurationService;
import com.similan.service.api.wall.WallService;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Slf4j
@Getter
@Setter
public abstract class BaseView implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String LAYOUT_MODE_LIST = "LIST";
  public static final String LAYOUT_MODE_GRID = "GRID";
  public static final String LAYOUT_MODE_THUMBNAIL = "THUMBNAIL";
  public static final String LAYOUT_MODE_SLIDE = "SLIDE";
  public static final String LAYOUT_MODE_WALL = "WALL";
  public static final String DEFAULT_COUNTRY = "United States";

  /* standard images */
  public static final String IMAGES_PRODUCT_DEFAULT_LOGO = "/images/businessLogo.jpg";
  public static final String IMAGES_MEMBER_DEFAULT_PHOTO = "/images/memberDefaultPhoto.png";
  public static final String IMAGES_DEFAULT_PARTNER_BANNER = "/images/bannerDefault.jpg";

  @ManagedProperty("#{embeddedUrlList}")
  @Autowired(required = false)
  protected transient Map<String, String> embeddedUrlMap;

  @ManagedProperty("#{campaignService}")
  @Autowired(required = false)
  protected transient CampaignService campaignService;

  @ManagedProperty("#{campaignManagementService}")
  @Autowired(required = false)
  protected transient CampaignManagementService campaignManagementService;

  @ManagedProperty("#{memberService}")
  @Autowired(required = false)
  protected transient MemberService memberService;

  @ManagedProperty("#{orgService}")
  @Autowired(required = false)
  protected transient OrganizationService orgService;

  @ManagedProperty("#{platformMetadataService}")
  @Autowired(required = false)
  protected transient PlatformMetadataService platformMetadataService;

  @ManagedProperty("#{facesHelper}")
  @Autowired(required = false)
  protected FacesHelper facesHelper;

  @ManagedProperty("#{imageUploadHandler}")
  @Autowired(required = false)
  protected transient ImageUploadHandler imageUploadHandler;

  @ManagedProperty("#{affiliateLeadService}")
  @Autowired(required = false)
  protected transient AffiliateLeadService affiliateLeadService;

  @ManagedProperty("#{domainEventService}")
  @Autowired(required = false)
  protected transient DomainEventService domianEventService;

  @ManagedProperty("#{searchService}")
  @Autowired(required = false)
  protected transient SearchService searchService;

  @ManagedProperty("#{socialService}")
  @Autowired(required = false)
  protected transient SocialService socialService;

  @ManagedProperty("#{platformCommonSettings}")
  @Autowired(required = false)
  protected transient PlatformCommonSettings platformCommonSettings;

  @ManagedProperty("#{documentInstanceServiceImpl}")
  @Autowired(required = false)
  protected transient DocumentInstanceService documentInstanceService;

  @ManagedProperty("#{commentServiceImpl}")
  @Autowired(required = false)
  protected transient CommentService commentService;

  @ManagedProperty("#{documentServiceImpl}")
  @Autowired(required = false)
  protected transient DocumentService documentService;

  @ManagedProperty("#{documentLabelServiceImpl}")
  @Autowired(required = false)
  protected transient DocumentLabelService documentLabelService;

  @ManagedProperty("#{documentCategoryServiceImpl}")
  @Autowired(required = false)
  protected transient DocumentCategoryService documentCategoryService;

  @ManagedProperty("#{managementWorkspaceServiceImpl}")
  @Autowired(required = false)
  protected transient ManagementWorkspaceService managementSpaceService;

  @ManagedProperty("#{socialActorServiceImpl}")
  @Autowired(required = false)
  protected transient SocialActorService socialActorService;

  @ManagedProperty("#{collaborationWorkspaceServiceImpl}")
  @Autowired(required = false)
  protected transient CollaborationWorkspaceService collabWorkspaceService;

  @ManagedProperty("#{collaborationWorkspaceSharedDocumentServiceImpl}")
  @Autowired(required = false)
  protected transient CollaborationWorkspaceSharedDocumentService collabDocumentShareService;

  @ManagedProperty("#{collaborationWorkspaceTaskServiceImpl}")
  @Autowired(required = false)
  protected transient CollaborationWorkspaceTaskService collabTaskService;

  @ManagedProperty("#{wallServiceImpl}")
  @Autowired(required = false)
  protected transient WallService wallService;

  @ManagedProperty("#{feedServiceImpl}")
  @Autowired(required = false)
  protected transient FeedService feedService;

  @ManagedProperty("#{connectionServiceImpl}")
  @Autowired(required = false)
  protected transient ConnectionService connectionService;

  @ManagedProperty("#{productService}")
  @Autowired(required = false)
  protected transient ProductService productService;

  @ManagedProperty("#{partnerManagementService}")
  @Autowired(required = false)
  protected transient PartnerManagementService partnerManagementService;

  @ManagedProperty("#{employeeRoleAdminServiceImpl}")
  @Autowired(required = false)
  protected transient EmployeeRoleAdminService employeeRoleAdminService;

  @ManagedProperty("#{businessProcessManagementServiceImpl}")
  @Autowired(required = false)
  protected transient BusinessProcessManagementService businessProcessManagementService;

  @ManagedProperty("#{boxClient}")
  @Autowired(required = false)
  protected transient BoxClient boxClient;

  @ManagedProperty("#{bookmarkServiceImpl}")
  @Autowired(required = false)
  protected transient BookmarkService bookmarkService;

  @ManagedProperty("#{notificationConfigurationServiceImpl}")
  @Autowired(required = false)
  protected transient NotificationConfigurationService notificationConfigurationService;

  @ManagedProperty("#{service_newsService}")
  @Autowired(required = false)
  protected transient NewsService newsService;

  @ManagedProperty("#{displayNoticeServiceImpl}")
  @Autowired(required = false)
  protected transient DisplayNoticeService displayNoticeService;

  @ManagedProperty("#{auditEventServiceImpl}")
  @Autowired(required = false)
  protected transient AuditEventService auditEventService;

  public Map<String, String> getEmbeddedUrlMap() {
    if (embeddedUrlMap == null) {
      embeddedUrlMap = new HashMap<String, String>();
      embeddedUrlMap.put("Partner Login", "partner/partnerBrandedLogin.xhtml");
    }
    return embeddedUrlMap;
  }

  public void setEmbeddedUrlMap(Map<String, String> embeddedUrlMap) {
    this.embeddedUrlMap = embeddedUrlMap;
  }

  public boolean isCreator(CollaborationWorkspaceDto workspace,
      SocialActorKey memberKey) {
    boolean owner = false;

    if (memberKey.getName().equalsIgnoreCase(workspace.getCreator().getName())) {
      owner = true;
    }

    return owner;
  }

  public List<CollaborationWorkspaceKey> getPartnerWorkspaces(
      OrganizationDetailInfoDto orgInfo, MemberInfoDto memberInfo) {
    return this.getPartnerManagementService()
        .getWorkspacesForParticipatingPartnerPrograms(orgInfo, memberInfo);
  }

  public String formatDate(Date date) {
    if (date == null)
      return "";
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    return format.format(date);
  }

  public String generateBoxToken(
      DocumentInstanceAndDocumentAndViewerElementsDto documentInstance)
      throws IOException {

    List<DocumentViewerItemDto> items = documentInstance.getItems();
    for (DocumentViewerItemDto item : items) {
      if (item.getType() == DocumentViewerItemType.ATTRIBUTE
          && "documentId".equals(item.getKey().getName())) {
        return boxClient.createDocumentSession(item.getValue());
      }
    }
    throw new IllegalStateException("Box document id not found: "
        + documentInstance.getKey());
  }

  public String generateCrocodocTokenEditable(
      DocumentInstanceAndDocumentAndViewerElementsDto document,
      MemberInfoDto member) throws CrocodocException {

    Map<String, Object> params = new HashMap<String, Object>();
    params.put(CrocodocConstants.IS_EDITABLE, Boolean.TRUE);

    // create the user
    Map<String, Object> user = new HashMap<String, Object>();
    user.put(CrocodocConstants.MEMBER_ID, member.getId());
    user.put(CrocodocConstants.MEMBER_NAME, member.getFirstName());

    // put it into params
    params.put(CrocodocConstants.MEMBER, user);

    List<DocumentViewerItemDto> items = document.getItems();
    for (DocumentViewerItemDto item : items) {
      if (item.getType() == DocumentViewerItemType.ATTRIBUTE
          && CrocodocConstants.DOCUMENT_ID.equals(item.getKey().getName())) {
        return CrocodocSession.create(item.getValue(), params);
      }
    }
    throw new IllegalStateException("Crocodoc document uuid not found: "
        + document.getKey());
  }

  public String generateCrocodocTokenReadOnly(
      DocumentInstanceAndDocumentAndViewerElementsDto documentInstance)
      throws CrocodocException {

    Map<String, Object> params = new HashMap<String, Object>();

    List<DocumentViewerItemDto> items = documentInstance.getItems();
    for (DocumentViewerItemDto item : items) {
      if (item.getType() == DocumentViewerItemType.ATTRIBUTE
          && "uuid".equals(item.getKey().getName())) {
        return CrocodocSession.create(item.getValue(), params);
      }
    }

    throw new IllegalStateException("Crocodoc document uuid not found: "
        + documentInstance.getDocument().getKey());
  }

  public String collaborationWorkspaceView(String urlPath,
      CollaborationWorkspaceKey workspaceKey) throws URISyntaxException {
    // URL
    String hostUrl = this.getPlatformCommonSettings().getPortalApplcationUrl()
        .getValue();
    URI uri = new URIBuilder(hostUrl + urlPath)
        .setParameter("wsname", workspaceKey.getName())
        .setParameter("owsname", workspaceKey.getOwner().getName()).build();

    String retUrl = uri.toString();
    log.info("Collaboration view url " + retUrl);
    return retUrl;
  }

  public String collaborationWorkspaceSharedDocumentView(String urlPath,
      SharedDocumentKey documentKey) throws URISyntaxException {
    // URL
    String hostUrl = this.getPlatformCommonSettings().getPortalApplcationUrl()
        .getValue();
    URI uri = new URIBuilder(hostUrl + urlPath).setParameter("sdoc",
        documentKey.getId().toString()).build();

    String retUrl = uri.toString();
    log.info("Collaboration view url " + retUrl);
    return retUrl;
  }

  public String managementWorkspaceView(String urlPath,
      ManagementWorkspaceKey workspaceKey) throws URISyntaxException {
    // URL
    String hostUrl = this.getPlatformCommonSettings().getPortalApplcationUrl()
        .getValue();
    URI uri = new URIBuilder(hostUrl + urlPath).setParameter("msname",
        workspaceKey.getName()).build();

    String retUrl = uri.toString();
    log.info("Management view url " + retUrl);
    return retUrl;
  }

  public String managementWorkspaceDocumentView(String urlPath,
      DocumentKey documentKey) throws URISyntaxException {
    // URL
    String hostUrl = this.getPlatformCommonSettings().getPortalApplcationUrl()
        .getValue();
    URI uri = new URIBuilder(hostUrl + urlPath)
        .setParameter("msname", documentKey.getWorkspace().getName())
        .setParameter("dname", documentKey.getName()).build();

    String retUrl = uri.toString();
    log.info("Management view url " + retUrl);
    return retUrl;
  }

  public List<StateType> getStatesForCountry(String country) {
    CountryBean countryBean = (CountryBean) this.findBackingBean("countryBean");

    List<StateType> selectedStateList = countryBean.getStateTypeList(country);

    log.info("Country " + country + " state list " + selectedStateList.size());

    return selectedStateList;
  }

  public List<DocumentInstanceAndDocumentModel> getAllManagementspaceDocuments(
      ManagementWorkspaceKey mgmtKey) {
    List<DocumentInstanceAndDocumentDto> results = managementSpaceService
        .getDocuments(mgmtKey);

    List<DocumentInstanceAndDocumentModel> documentList = new ArrayList<DocumentInstanceAndDocumentModel>();
    for (DocumentInstanceAndDocumentDto dto : results) {
      DocumentInstanceAndDocumentModel model = new DocumentInstanceAndDocumentModel(
          dto);
      documentList.add(model);

    }

    return documentList;
  }

  protected boolean containsIgnoreCase(List<String> strList,
      String comparedString) {
    Iterator<String> it = strList.iterator();

    while (it.hasNext()) {
      if (it.next().equalsIgnoreCase(comparedString))
        return true;
    }

    return false;
  }

  protected SocialActorKey getSocialActorKey(Long actorId) {
    return this.getSocialActorService().transitional_getKey(actorId);
  }

  protected SocialActorKey getOrgKey(OrganizationDetailInfoDto orgInfo) {
    String name = orgInfo.getName();
    if (StringUtils.isBlank(name)) {
      return this.getSocialActorService().transitional_getKey(orgInfo.getId());
    }

    SocialActorKey retKey = new SocialActorKey(name);
    retKey.setId(orgInfo.getId());
    return retKey;

  }

  protected SocialActorKey getMemberKey(MemberInfoDto memberInfo) {
    String name = memberInfo.getName();
    if (StringUtils.isBlank(name)) {
      return this.getSocialActorService().transitional_getKey(
          memberInfo.getId());
    }

    SocialActorKey retKey = new SocialActorKey(name);
    retKey.setId(memberInfo.getId());
    return retKey;
  }

  public String getCommentAuthorId(SocialActorKey key) {
    try {
      Long memberId = socialActorService.transitional_getId(key);
      return String.valueOf(memberId);
    } catch (Exception e) {
      return null;
    }

  }

  public String getFormattedDate(Date date) {
    Date formattedDate = new Date();
    if (date != null) {
      formattedDate = date;
    }
    String newstring = new SimpleDateFormat("MM-dd-yyyy").format(formattedDate);
    return newstring;
  }

  public String getCommentAuthorPhotoLocation(SocialActorKey key) {
    try {
      ActorDto socialActor = this.socialActorService.getActor(key);
      String photoLocation = PhotoViewOption.ShowPhoto.effectivePhoto(
          IMAGES_MEMBER_DEFAULT_PHOTO, socialActor.getDisplayImage());
      return photoLocation;

    } catch (Exception e) {
      return null;
    }
  }

  public String getWorkspaceLogo(String logo) {
    String photoLocation = PhotoViewOption.ShowPhoto.effectivePhoto(
        "images/businessLogo.jpg", logo);
    log.info("Photo location " + photoLocation);
    return photoLocation;
  }

  public String getWorkspaceLogoByKey(CollaborationWorkspaceKey workspaceKey) {
    CollaborationWorkspaceDto collabSpace = this.getCollabWorkspaceService()
        .get(workspaceKey);
    return PhotoViewOption.ShowPhoto.effectivePhoto("images/businessLogo.jpg",
        collabSpace.getLogo());
  }

  public String getCommentAuthorFullName(SocialActorKey key) {
    try {
      Long memberId = socialActorService.transitional_getId(key);
      MemberInfoDto member = memberService.getMemberById(memberId);

      return member.getFullName();
    } catch (Exception e) {
      return null;
    }
  }

  public String getWallEntryType(WallEntryType entryType) {
    return entryType.name();
  }

  public String getFeedEntryType(FeedEntryType entryType) {
    return entryType.name();
  }

  public List<SelectableContact> getContacts(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo) {
    List<SelectableContact> contacts = new ArrayList<SelectableContact>();

    try {
      List<SocialActorContactDto> contactList = this.getNetworkService()
          .getExtendedConnections(this.getMemberKey(member));

      if (contactList != null) {
        for (SocialActorContactDto contact : contactList) {
          SelectableContact selectableContact = new SelectableContact(contact);
          selectableContact.setContactSource(ContactSource.Member);
          contacts.add(selectableContact);
        }
      }
    } catch (Exception exception) {
      log.error("Unable to get contacts ", exception);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contacts",
              "Unable to retrieve your connections."));
    }

    return contacts;
  }

  public NotificationConfigurationService getNotificationConfigurationService() {
    return notificationConfigurationService;
  }

  public void setNotificationConfigurationService(
      NotificationConfigurationService notificationConfigurationService) {
    this.notificationConfigurationService = notificationConfigurationService;
  }

  public BookmarkService getBookmarkService() {
    return bookmarkService;
  }

  public void setBookmarkService(BookmarkService bookmarkService) {
    this.bookmarkService = bookmarkService;
  }

  public BoxClient getBoxClient() {
    return boxClient;
  }

  public void setBoxClient(BoxClient boxClient) {
    this.boxClient = boxClient;
  }

  public BusinessProcessManagementService getBusinessProcessManagementService() {
    return businessProcessManagementService;
  }

  public void setBusinessProcessManagementService(
      BusinessProcessManagementService businessProcessManagementService) {
    this.businessProcessManagementService = businessProcessManagementService;
  }

  public EmployeeRoleAdminService getEmployeeRoleAdminService() {
    return employeeRoleAdminService;
  }

  public void setEmployeeRoleAdminService(
      EmployeeRoleAdminService employeeRoleAdminService) {
    this.employeeRoleAdminService = employeeRoleAdminService;
  }

  public CampaignManagementService getCampaignManagementService() {
    return campaignManagementService;
  }

  public void setCampaignManagementService(
      CampaignManagementService campaignManagementService) {
    this.campaignManagementService = campaignManagementService;
  }

  public PartnerManagementService getPartnerManagementService() {
    return partnerManagementService;
  }

  public void setPartnerManagementService(
      PartnerManagementService partnerManagementService) {
    this.partnerManagementService = partnerManagementService;
  }

  public ProductService getProductService() {
    return productService;
  }

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  public ConnectionService getNetworkService() {
    return connectionService;
  }

  public void setNetworkService(ConnectionService connectionService) {
    this.connectionService = connectionService;
  }

  public FeedService getFeedService() {
    return feedService;
  }

  public void setFeedService(FeedService feedService) {
    this.feedService = feedService;
  }

  public WallService getWallService() {
    return wallService;
  }

  public void setWallService(WallService wallService) {
    this.wallService = wallService;
  }

  public CollaborationWorkspaceSharedDocumentService getCollabDocumentShareService() {
    return collabDocumentShareService;
  }

  public void setCollabDocumentShareService(
      CollaborationWorkspaceSharedDocumentService collabDocumentShareService) {
    this.collabDocumentShareService = collabDocumentShareService;
  }

  public CollaborationWorkspaceTaskService getCollabTaskService() {
    return collabTaskService;
  }

  public void setCollabTaskService(
      CollaborationWorkspaceTaskService collabTaskService) {
    this.collabTaskService = collabTaskService;
  }

  public SocialActorService getSocialActorService() {
    return socialActorService;
  }

  public void setSocialActorService(SocialActorService socialActorService) {
    this.socialActorService = socialActorService;
  }

  public ManagementWorkspaceService getManagementSpaceService() {
    return managementSpaceService;
  }

  public void setManagementSpaceService(
      ManagementWorkspaceService managementSpaceService) {
    this.managementSpaceService = managementSpaceService;
  }

  public DocumentService getDocumentService() {
    return documentService;
  }

  public void setDocumentService(DocumentService documentService) {
    this.documentService = documentService;
  }

  public CollaborationWorkspaceService getCollabWorkspaceService() {
    return collabWorkspaceService;
  }

  public void setCollabWorkspaceService(
      CollaborationWorkspaceService collabWorkspaceService) {
    this.collabWorkspaceService = collabWorkspaceService;
  }

  public SocialService getSocialService() {
    return socialService;
  }

  public void setSocialService(SocialService socialService) {
    this.socialService = socialService;
  }

  public SearchService getSearchService() {
    return searchService;
  }

  public void setSearchService(SearchService searchService) {
    this.searchService = searchService;
  }

  public DomainEventService getDomianEventService() {
    return domianEventService;
  }

  public void setDomianEventService(DomainEventService domianEventService) {
    this.domianEventService = domianEventService;
  }

  public CampaignService getCampaignService() {
    return campaignService;
  }

  public void setCampaignService(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  public AffiliateLeadService getAffiliateLeadService() {
    return affiliateLeadService;
  }

  public void setAffiliateLeadService(AffiliateLeadService affiliateLeadService) {
    this.affiliateLeadService = affiliateLeadService;
  }

  public ImageUploadHandler getImageUploadHandler() {
    return imageUploadHandler;
  }

  public void setImageUploadHandler(ImageUploadHandler assetStorage) {
    this.imageUploadHandler = assetStorage;
  }

  public PlatformMetadataService getPlatformMetadataService() {
    return platformMetadataService;
  }

  public void setPlatformMetadataService(
      PlatformMetadataService platformMetadataService) {
    this.platformMetadataService = platformMetadataService;
  }

  public MemberService getMemberService() {
    return memberService;
  }

  public void setMemberService(MemberService memberService) {
    this.memberService = memberService;
  }

  public void setFacesHelper(FacesHelper facesHelper) {
    this.facesHelper = facesHelper;
  }

  public OrganizationService getOrgService() {
    return orgService;
  }

  public void setOrgService(OrganizationService orgService) {
    this.orgService = orgService;
  }

  protected Object findBackingBean(String beanName) {
    return JsfUtils.findBackingBean(beanName);
  }

  protected String getContextParam(String paramName) {
    return JsfUtils.getContextParam(paramName);
  }

  public String convertDateToString(Date date) {
    if (date == null)
      return null;
    return new SimpleDateFormat("MM/dd/yyyy").format(date);
  }

  public PlatformCommonSettings getPlatformCommonSettings() {
    return platformCommonSettings;
  }

  public void setPlatformCommonSettings(
      PlatformCommonSettings platformCommonSettings) {
    this.platformCommonSettings = platformCommonSettings;
  }

  public DocumentInstanceService getDocumentInstanceService() {
    return documentInstanceService;
  }

  public void setDocumentInstanceService(
      DocumentInstanceService documentInstanceService) {
    this.documentInstanceService = documentInstanceService;
  }

  public void setCommentService(CommentService commentService) {
    this.commentService = commentService;
  }

  public CommentService getCommentService() {
    return commentService;
  }

  public String ago(Date date) {
    return new PrettyTime().format(date);
  }

  public void setDocumentCategoryService(
      DocumentCategoryService documentCategoryService) {
    this.documentCategoryService = documentCategoryService;
  }

  public DocumentCategoryService getDocumentCategoryService() {
    return documentCategoryService;
  }

  public void setDocumentLabelService(DocumentLabelService documentLabelService) {
    this.documentLabelService = documentLabelService;
  }

  public DocumentLabelService getDocumentLabelService() {
    return documentLabelService;
  }

  /**
   * 
   * @param in
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readObject(java.io.ObjectInputStream in) throws IOException,
      ClassNotFoundException {

    in.defaultReadObject();
    this.campaignService = (CampaignService) JsfUtils
        .findBackingBean("campaignService");
    this.imageUploadHandler = (ImageUploadHandler) JsfUtils
        .findBackingBean("imageUploadHandler");
    this.memberService = (MemberService) JsfUtils
        .findBackingBean("memberService");
    this.orgService = (OrganizationService) JsfUtils
        .findBackingBean("orgService");
    this.platformMetadataService = (PlatformMetadataService) JsfUtils
        .findBackingBean("platformMetadataService");
    this.affiliateLeadService = (AffiliateLeadService) JsfUtils
        .findBackingBean("affiliateLeadService");
    this.domianEventService = (DomainEventService) JsfUtils
        .findBackingBean("domainEventService");
    this.searchService = (SearchService) JsfUtils
        .findBackingBean("searchService");
  }

}