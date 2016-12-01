package com.similan.portal.view.collabspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.model.DocumentInstanceAndDocumentModel;
import com.similan.portal.model.SelectableCollaborationWorkspaceModel;
import com.similan.portal.model.SelectableContact;
import com.similan.portal.model.SelectableDocumentInstanceAndDocumentModel;
import com.similan.portal.view.wall.WallView;
import com.similan.service.api.advertisement.dto.basic.DisplayNoticeDto;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.bookmark.dto.operation.NewBookmarkDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentAndStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewDocumentSharedWithOtherWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInviteDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewSharedDocumentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceDto;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceType;
import com.similan.service.api.wall.dto.basic.WallDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;

@Scope("view")
@Component("workspaceDetailView")
@Slf4j
public class CollabWorkspaceDetailView extends CollabWorkspaceDocumentViewUtil
    implements WallView<CollaborationWorkspaceKey> {

  private static final long serialVersionUID = 1L;
  private static final int MINIMUM_NUMBER_OF_ENTRY = 1;

  @Autowired
  private MemberInfoDto member;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private CollaborationWorkspaceDto workspace;

  private List<TaskDto> openTasks = null;

  private List<SelectableContact> contacts = null;

  private List<DocumentInstanceAndDocumentModel> documents = new ArrayList<DocumentInstanceAndDocumentModel>();

  private SelectableDocumentInstanceAndDocumentModel selectableDocuments = null;

  private DocumentInstanceAndDocumentModel[] selectedDocs = null;

  private Map<String, SharedDocumentDto> sharedDocumentMap = new HashMap<String, SharedDocumentDto>();

  private List<ActorDto> participants = null;

  private List<WallEntryDto<CollaborationWorkspaceKey>> wallEntries = null;

  private List<LinkReferenceDto> externalLinkEntries = null;

  private List<LinkReferenceDto> externalLinkUnfilteredEntries = null;

  private WallDto<CollaborationWorkspaceKey> workspaceWall = null;

  private SelectableCollaborationWorkspaceModel sharableSpaces = null;

  private CollaborationWorkspaceDto[] selectedWorkspaces = null;

  private String postContent;

  private SharedDocumentKey toBeShared = null;

  private SocialActorKey accessorKey = null;

  private boolean bookmarked = false;

  private List<ExternalBusinessPortalDto> externalPortals = null;

  private List<NewsDto> newsList = null;

  private DisplayNoticeDto activeNotice = null;

  private String externalLinkText = StringUtils.EMPTY;

  @PostConstruct
  public void init() {
    log.info("Initializing workspace detail view");

    String workSpaceName = this.getContextParam("wsname");
    String workSpaceOwnerName = this.getContextParam("owsname");
    log.info("Workspace name " + workSpaceName + " owner name "
        + workSpaceOwnerName);

    if (workSpaceName != null) {
      CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(
          workSpaceOwnerName, workSpaceName);

      accessorKey = this.getMemberKey(member);

      try {
        this.workspace = this.getCollabWorkspaceService().getDetail(
            workspaceKey);
        bookmarked = this.getBookmarkService().isBookmarked(accessorKey,
            (IBookmarkableKey) workspaceKey);

        this.fetchSharedDocs(workspaceKey);
        this.fetchSelectableDocuments();

        this.openTasks = this.getCollabTaskService().getAllOpenTasks(
            workspaceKey, this.getMemberKey(member));
        this.participants = this.getCollabWorkspaceService().getParticipants(
            workspaceKey);

        WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(
            this.workspace.getKey());
        workspaceWall = this.getWallService().get(wallKey);
        this.fetchLatestWallEntries();

        /*
         * if partner portal workspace then fetch the relevant external links
         */
        Boolean partnerWorkspace = this.workspace.getPartnerWorkspace();
        if (partnerWorkspace != null && partnerWorkspace != Boolean.FALSE) {

          SocialActorKey ownerKey = this.workspace.getKey().getOwner();
          Long workspaceOwnerId = this.getSocialActorService()
              .transitional_getId(ownerKey);
          log.info("Workspace owner id " + workspaceOwnerId);

          this.externalPortals = this.getOrgService().getExternalPortals(
              workspaceOwnerId);

          this.newsList = this.getNewsService().getLatest(
              workspace.getKey().getOwner(), this.getMemberKey(member));

          List<DisplayNoticeDto> noticeList = this.getDisplayNoticeService()
              .getActive(workspace.getKey().getOwner());

          // for now get the 1st one
          if (noticeList != null && noticeList.size() > 0) {
            activeNotice = noticeList.get(0);
          }
        }

      } catch (Exception exp) {

        log.error("Security exception ", exp);
        FacesContext
            .getCurrentInstance()
            .addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error",
                    "You do not have access to this function for the moment. Please have "
                        + "your Admin invite you to the workspace"
                        + workSpaceName
                        + " within the function, then you "
                        + "will have Admin Rights to create new workspaces or programs "
                        + "within the function. "));
      }

    }
  }

  public String getExternalLinkText() {
    return externalLinkText;
  }

  public void setExternalLinkText(String externalLinkText) {
    this.externalLinkText = externalLinkText;
  }

  public DisplayNoticeDto getActiveNotice() {
    return activeNotice;
  }

  public List<NewsDto> getNewsList() {
    return newsList;
  }

  public SocialActorKey getAccessorKey() {
    return accessorKey;
  }

  public boolean getBookmarked() {
    return this.bookmarked;
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public List<ExternalBusinessPortalDto> getExternalPortals() {
    return externalPortals;
  }

  public List<SocialActorContactDto> getContactList() {
    SocialActorKey actorKey = this.getMemberKey(member);
    List<SocialActorContactDto> contactList = this.getNetworkService()
        .getExtendedConnections(actorKey);
    return contactList;
  }

  public SelectableCollaborationWorkspaceModel getSharableSpaces() {
    if (sharableSpaces == null) {
      List<CollaborationWorkspaceDto> wsList = this.collabWorkspaceService
          .getForPerticipant(this.getMemberKey(member));
      for (CollaborationWorkspaceDto ws : wsList) {
        if (ws.getKey().equals(this.workspace.getKey())) {
          wsList.remove(ws);
          break;
        }
      }
      sharableSpaces = new SelectableCollaborationWorkspaceModel(wsList);
    }
    return sharableSpaces;
  }

  public void setSharableSpaces(
      SelectableCollaborationWorkspaceModel sharableSpaces) {
    this.sharableSpaces = sharableSpaces;
  }

  public CollaborationWorkspaceDto[] getSelectedWorkspaces() {
    return selectedWorkspaces;
  }

  public void setSelectedWorkspaces(
      CollaborationWorkspaceDto[] selectedWorkspaces) {
    this.selectedWorkspaces = selectedWorkspaces;
  }

  public String getWorkspaceLogo() {
    return PhotoViewOption.ShowPhoto.effectivePhoto("images/businessLogo.jpg",
        workspace.getLogo());
  }

  public String getWorkspaceOwnerLogo() {
    SocialActorKey ownerKey = this.workspace.getKey().getOwner();
    ActorDto ownerBusiness = this.getSocialActorService().getActor(ownerKey);
    return PhotoViewOption.ShowPhoto.effectivePhoto("images/businessLogo.jpg",
        ownerBusiness.getDisplayImage());
  }

  public Long getWorkspaceOwnerId() {
    SocialActorKey ownerKey = this.workspace.getKey().getOwner();
    return this.getSocialActorService().transitional_getId(ownerKey);
  }

  public String getNumberParticipants() {
    if (this.participants != null) {
      return String.valueOf(this.participants.size());
    }

    return String.valueOf(0);
  }

  private void fetchLatestWallEntries() {

    wallEntries = this.getCollabWorkspaceService().getWallEntries(
        this.workspace.getKey(), this.accessorKey);

  }

  public MemberInfoDto getMember() {
    return member;
  }

  public void setMember(MemberInfoDto member) {
    this.member = member;
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }

  public WallDto<CollaborationWorkspaceKey> getWorkspaceWall() {
    return workspaceWall;
  }

  public List<WallEntryDto<CollaborationWorkspaceKey>> getWallEntries() {
    return wallEntries;
  }

  public List<ActorDto> getParticipants() {
    return participants;
  }

  public void setParticipants(List<ActorDto> participants) {
    this.participants = participants;
  }

  public DocumentInstanceAndDocumentModel[] getSelectedDocs() {
    return selectedDocs;
  }

  public void setSelectedDocs(DocumentInstanceAndDocumentModel[] selectedDocs) {
    this.selectedDocs = selectedDocs;
  }

  public SelectableDocumentInstanceAndDocumentModel getSelectableDocuments() {
    return selectableDocuments;
  }

  public void setSelectableDocuments(
      SelectableDocumentInstanceAndDocumentModel selectableDocuments) {
    this.selectableDocuments = selectableDocuments;
  }

  public List<SelectableContact> getContacts() {
    if (contacts == null) {
      contacts = this.fetchInvitableContacts();
    }
    return contacts;
  }

  public List<DocumentInstanceAndDocumentModel> getDocuments() {
    return documents;
  }

  public void setDocuments(List<DocumentInstanceAndDocumentModel> documents) {
    this.documents = documents;
  }

  public void setContacts(List<SelectableContact> contacts) {
    this.contacts = contacts;
  }

  public CollaborationWorkspaceDto getWorkspace() {
    return workspace;
  }

  public void setWorkspace(CollaborationWorkspaceDto workspace) {
    this.workspace = workspace;
  }

  public List<TaskDto> getOpenTasks() {
    return openTasks;
  }

  public void setOpenTasks(List<TaskDto> openTasks) {
    this.openTasks = openTasks;
  }

  public SharedDocumentKey getToBeShared() {
    return toBeShared;
  }

  public void setToBeShared(SharedDocumentKey toBeShared) {
    log.info("To be shared " + toBeShared);
    this.toBeShared = toBeShared;
  }

  public int getNumberOpenTasks() {
    if (this.openTasks != null) {
      return this.openTasks.size();
    } else {
      return 0;
    }
  }

  public List<LinkReferenceDto> getExternalLinkEntries() {
    if (externalLinkEntries == null) {
      this.fetchExternalLinkEntries();
    }

    return externalLinkEntries;
  }

  private void fetchExternalLinkEntries() {
    this.externalLinkEntries = this.getCollabWorkspaceService()
        .getAllExternalLinkReferencePosts(this.workspace.getKey(),
            this.accessorKey);
    if (externalLinkEntries != null) {
      log.info("Number of entries " + externalLinkEntries.size());
    }

    this.externalLinkUnfilteredEntries = this.externalLinkEntries;
  }

  public void externalLinkFilterByType(String linkType) {
    if (StringUtils.equalsIgnoreCase(linkType, "ALL")) {
      this.externalLinkEntries = this.externalLinkUnfilteredEntries;
      return;
    }

    if (StringUtils.isBlank(linkType) != true) {
      this.externalLinkEntries = new ArrayList<LinkReferenceDto>();
      for (LinkReferenceDto externLink : this.externalLinkUnfilteredEntries) {

        LinkReferenceType linkRefType = externLink.getLinkReferenceType();
        if (linkRefType != null) {
          String externLinkType = linkRefType.toString();
          if (StringUtils.equals(externLinkType, linkType)) {
            this.externalLinkEntries.add(externLink);
          }
        }

      }
    } else {
      this.externalLinkEntries = this.externalLinkUnfilteredEntries;
    }
  }

  public void externalLinkSearch() {
    log.info("Searching for " + this.externalLinkText
        + " in external link list");

    if (StringUtils.isBlank(this.externalLinkText) != true) {
      this.externalLinkEntries = new ArrayList<LinkReferenceDto>();
      for (LinkReferenceDto externLink : this.externalLinkUnfilteredEntries) {

        String externLinkTitle = StringUtils.lowerCase(externLink.getTitle());
        String externLinkContent = StringUtils.lowerCase(externLink
            .getContent());

        if (externLinkTitle.contains(StringUtils
            .lowerCase(this.externalLinkText))
            || externLinkContent.contains(StringUtils
                .lowerCase(this.externalLinkText))) {
          this.externalLinkEntries.add(externLink);
        }
      }
    } else {
      this.externalLinkEntries = this.externalLinkUnfilteredEntries;
    }

    this.externalLinkText = StringUtils.EMPTY;
  }

  public void moreWallEntries() {

    if (this.wallEntries != null
        && this.wallEntries.size() > MINIMUM_NUMBER_OF_ENTRY) {
      WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(
          this.workspace.getKey());
      WallEntryDto<CollaborationWorkspaceKey> lastEntry = this.wallEntries
          .get(this.wallEntries.size() - 1);

      log.info("Last current entry " + lastEntry.getDate() + " entry number "
          + this.wallEntries.size());
      List<WallEntryDto<CollaborationWorkspaceKey>> moreWallEntries = this
          .getWallService().getMore(wallKey, lastEntry.getKey().getNumber());

      if (moreWallEntries != null && moreWallEntries.size() > 0) {
        log.info("Number of entry fetched " + moreWallEntries.size());
        this.wallEntries.addAll(moreWallEntries);
      }
    }

  }

  private void fetchSharedDocs(CollaborationWorkspaceKey workspaceKey) {
    SocialActorKey viewerKey = this.getMemberKey(member);
    this.getSharedDocs(workspaceKey, viewerKey);

    for (SharedDocumentAndStatisticsDto sharedDto : this.getWorkspaceDocs()) {
      log.info("Shared document key "
          + sharedDto.getKey().getDocument().getName());
      this.sharedDocumentMap.put(sharedDto.getSharedDocument().getKey()
          .getDocument().getName(), sharedDto.getSharedDocument());
    }
  }

  public String getWorkspaceMembers() {
    /**
     * <table class="table table-striped">
     * <tr>
     * <th>Rank</th>
     * </tr>
     * </table>
     */
    StringBuilder members = new StringBuilder()
        .append("<table class=\"table table-striped\">");
    for (ActorDto participant : this.getParticipants()) {
      members.append("<tr>");
      members.append("<th>");
      members.append("<b>");
      members.append(participant.getDisplayName());
      members.append("</b>");
      members.append("</th>");
      members.append("</tr>");
    }

    members.append("</table>");
    return members.toString();
  }

  public String getWorkspaceDocuments() {
    /**
     * <table class="table table-striped">
     * <tr>
     * <th>Rank</th>
     * </tr>
     * </table>
     */
    StringBuilder members = new StringBuilder()
        .append("<table class=\"table table-striped\">");
    for (SharedDocumentAndStatisticsDto document : this.getWorkspaceDocs()) {
      members.append("<tr>");
      members.append("<th>");
      members.append("<b>");
      members.append(document.getSharedDocument().getKey().getDocument()
          .getName());
      members.append("</b>");
      members.append("</th>");
      members.append("</tr>");
    }

    members.append("</table>");
    return members.toString();
  }

  private void fetchSelectableDocuments() {
    List<ManagementWorkspaceDto> mgmtSpaceList = this
        .getManagementSpaceService().getForParticipant(
            this.getMemberKey(member));

    List<DocumentInstanceAndDocumentModel> docList = new ArrayList<DocumentInstanceAndDocumentModel>();
    for (ManagementWorkspaceDto mgmtSpace : mgmtSpaceList) {
      log.info("Management space found " + mgmtSpace.getKey().getName());
      docList.addAll(this.getAllManagementspaceDocuments(mgmtSpace.getKey()));
    }

    for (DocumentInstanceAndDocumentModel doc : docList) {
      log.info("Key to be evaluated "
          + doc.getData().getDocument().getKey().getName());

      if (this.sharedDocumentMap.containsKey(doc.getData().getDocument()
          .getKey().getName()) != true) {
        log.info("Document found to be shared "
            + doc.getData().getDocument().getKey().getName());
        documents.add(doc);
      }
    }

    this.selectableDocuments = new SelectableDocumentInstanceAndDocumentModel(
        documents);
  }

  private List<SelectableContact> fetchInvitableContacts() {
    List<SelectableContact> allContacts = this.getContacts(member, orgInfo);

    if (allContacts != null) {
      contacts = new ArrayList<SelectableContact>();
      for (SelectableContact selectContact : allContacts) {

        if (selectContact.getIsBusiness() == true) {
          continue;
        }

        if (this.participants != null) {

          SocialActorKey key = selectContact.getContact().getKey();
          boolean isParticipant = false;

          for (ActorDto part : this.participants) {
            if (part.getKey().equals(key) == true) {
              isParticipant = true;
              break;
            }
          }

          if (isParticipant != true) {
            contacts.add(selectContact);
          }
        } else {
          contacts.add(selectContact);
        }
      }
    }
    return contacts;
  }

  public void shareDocument() {

    try {
      if (this.selectedDocs != null) {
        for (DocumentInstanceAndDocumentModel doc : this.selectedDocs) {
          log.info("Sharing document " + doc.getData().getDocument().getKey());
          boolean isSharable = true;
          if (this.getWorkspaceDocs() != null) {
            if (this.sharedDocumentMap.containsKey(doc.getData().getDocument()
                .getKey().getName())) {
              FacesContext.getCurrentInstance().addMessage(
                  null,
                  new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
                      "Cannot share the same document again"
                          + doc.getData().getDocument().getKey().getName()));
              isSharable = false;

            }
          }

          if (isSharable == true) {
            SharedDocumentKey sharedDocKey = new SharedDocumentKey(
                this.workspace.getKey(), doc.getData().getDocument().getKey());
            NewSharedDocumentDto shareDoc = new NewSharedDocumentDto(
                this.getMemberKey(member));
            this.getCollabDocumentShareService().share(sharedDocKey, shareDoc);
          }
        }

        this.fetchSharedDocs(this.workspace.getKey());
        this.fetchSelectableDocuments();

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Status",
                "Thanks for sharing the document in this workspace"));
      } else {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
                "Please selct a document to be shared"));
      }
    } catch (Exception exp) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
              "Please selct a document to be shared " + exp.getMessage()));
    }
  }

  public void post() {

    try {
      /* create a wall post */
      NewWallPostDto newPost = new NewWallPostDto(this.getMemberKey(member),
          this.postContent);
      this.getCollabWorkspaceService().post(this.workspaceWall.getKey(),
          newPost);

      /* also goes to feed */
      this.fetchLatestWallEntries();

      /* get the links references */
      this.fetchExternalLinkEntries();

    } catch (Exception exp) {
      log.error("Error", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Comment cannot be posted for " + exp.getMessage()));
    }

    this.postContent = StringUtils.EMPTY;
  }

  public void invite() {

    try {
      // Initiate the invite process.
      List<SocialActorKey> participants = new ArrayList<SocialActorKey>();

      for (SelectableContact contact : contacts) {
        if (contact.isSelected()) {
          SocialActorKey key = contact.getContact().getContact().getKey();
          log.info("About to invite " + contact.getFullName());
          participants.add(key);
        }
      }

      NewInviteDto invite = new NewInviteDto(workspace.getKey(), participants);
      collabWorkspaceService.invite(invite);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "An invitation email was sent to the invitees. You "
                  + "will be notified upon acceptance."));

    } catch (Exception exp) {

      log.error("Error in workspace invite", exp);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot invite due to : " + exp.getMessage()));
    }

  }

  public void bookmark() {
    log.info("Bookmark the workspace ");
    try {

      StringBuilder bookMarkName = new StringBuilder().append(
          "Collaboration workspace ").append(this.workspace.getKey().getName());

      NewBookmarkDto newBookmarkDto = new NewBookmarkDto(accessorKey,
          (IBookmarkableKey) this.workspace.getKey(), bookMarkName.toString());

      this.getBookmarkService().create(newBookmarkDto);

      bookmarked = true;
    } catch (Exception exp) {
      log.error("Error in bookmarking ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error in bookmarking the document "));

    }
  }

  public void shareDocumentWithOtherSpace() {
    log.info("Sharing document " + this.toBeShared + " with workspaces "
        + this.selectedWorkspaces);

    if (this.selectedWorkspaces != null && this.toBeShared != null) {
      for (CollaborationWorkspaceDto shareSpace : this.selectedWorkspaces) {

        SharedDocumentKey sharedDocKey = new SharedDocumentKey(
            shareSpace.getKey(), this.toBeShared.getDocument());
        NewDocumentSharedWithOtherWorkspaceDto shareDoc = new NewDocumentSharedWithOtherWorkspaceDto(
            this.getMemberKey(member), sharedDocKey.getWorkspace());
        try {
          this.getCollabDocumentShareService().share(sharedDocKey, shareDoc);
        } catch (Exception exp) {
          log.error("Cannot share document", exp);
          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  "Cannot share the the document with workspace "
                      + shareSpace.getKey().getName()));

        }

      }
    }

    this.selectedWorkspaces = null;
    this.toBeShared = null;

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Status",
            "Thanks for sharing the document with other workspaces"));
  }

}
