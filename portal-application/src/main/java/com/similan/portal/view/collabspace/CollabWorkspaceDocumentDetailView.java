package com.similan.portal.view.collabspace;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.crocodoc.CrocodocException;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.model.DocumentPageModel;
import com.similan.portal.model.SelectableCollaborationWorkspaceModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.bookmark.dto.operation.NewBookmarkDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentAndStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.extended.SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewDocumentSharedWithOtherWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewExternalSharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewSharedDocumentCommentDto;
import com.similan.service.api.comment.dto.basic.CommentReplyDto;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.api.comment.dto.operation.NewCommentReplyDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentPageDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallKey;

@Scope("view")
@Component("workspaceDocumentDetailView")
@Slf4j
public class CollabWorkspaceDocumentDetailView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto memberInfo;

  private SocialActorKey viewerKey;

  private CollaborationWorkspaceDto workspace = null;

  private SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto sharedDoc = null;

  private List<CommentAndRepliesDto<SharedDocumentKey>> wallComments = null;

  private String pageComment = StringUtils.EMPTY;

  private String wallComment = StringUtils.EMPTY;

  private String layoutMode = BaseView.LAYOUT_MODE_THUMBNAIL;

  private DocumentPageModel selectedPage = null;

  private List<DocumentPageModel> pages = new ArrayList<DocumentPageModel>();

  private SharedDocumentAndStatisticsDto sharedDocStatistics = null;

  private List<CollaborationWorkspaceParticipationDto> participants = null;

  private SelectableCollaborationWorkspaceModel sharableSpaces = null;

  private CollaborationWorkspaceDto[] selectedWorkspaces = null;

  private List<WallEntryDto<CollaborationWorkspaceKey>> docWallEntryList = null;

  private boolean bookmarked = false;

  private String firstName;

  private String lastName;

  private String email;

  private String mobilePhone;

  private String header;

  private String message;

  @PostConstruct
  public void init() {

    String sharedDocParam = this.getContextParam("sdoc");

    log.info("Workspace shared document id " + sharedDocParam);

    try {

      if (sharedDocParam != null) {

        Long sharedDocumentId = Long.valueOf(sharedDocParam);
        viewerKey = this.getMemberKey(memberInfo);
        sharedDoc = this.getCollabDocumentShareService().getForViewer(
            sharedDocumentId, viewerKey);

        this.workspace = this.getCollabWorkspaceService().get(
            new CollaborationWorkspaceKey(sharedDoc.getKey().getWorkspace()
                .getOwner().getName(), sharedDoc.getKey().getWorkspace()
                .getName()));

        this.participants = this.getCollabWorkspaceService().getParticipations(
            this.workspace.getKey());
      }

      bookmarked = this.getBookmarkService().isBookmarked(viewerKey,
          (IBookmarkableKey) sharedDoc.getKey());

      sharedDocStatistics = this.getCollabDocumentShareService()
          .getSharedDocumentAndStatistics(sharedDoc.getKey());

      List<DocumentPageDto> pageList = sharedDoc.getDocumentInstance()
          .getPages();
      for (DocumentPageDto pageDto : pageList) {
        DocumentPageModel pageModel = new DocumentPageModel(pageDto);
        pageModel
            .setComments(this.commentService.getComments(pageDto.getKey()));
        this.pages.add(new DocumentPageModel(pageDto));
      }
      if (pages != null && pages.size() > 0) {
        selectedPage = pages.get(0);
      }

      this.wallComments = this.commentService.getComments(sharedDoc.getKey());

    } catch (Exception exp) {
      log.error("Cannot fetch document", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Cannot fetch shared document " + exp.getMessage()));
    }

  }

  public SelectableCollaborationWorkspaceModel getSharableSpaces() {
    if (sharableSpaces == null) {
      List<CollaborationWorkspaceDto> wsList = this.collabWorkspaceService
          .getForPerticipant(this.getMemberKey(memberInfo));
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

  public List<WallEntryDto<CollaborationWorkspaceKey>> getDocWallEntryList() {
    if (docWallEntryList == null) {
      WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(
          this.workspace.getKey());
      docWallEntryList = this.getWallService().getWorkspaceDocumentHistory(
          wallKey, this.sharedDoc.getKey());
    }
    return docWallEntryList;
  }

  public SocialActorKey getViewerKey() {
    return viewerKey;
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

  public List<DocumentLabelKey> getDocumentLabel() {
    List<DocumentLabelKey> retLabels = this.sharedDoc.getDocumentInstance()
        .getDocument().getLabels();
    if (retLabels == null || retLabels.size() <= 0) {
      retLabels = new ArrayList<DocumentLabelKey>();
      DocumentLabelKey label = new DocumentLabelKey(this.sharedDoc.getSharer(),
          "No labels defined");
      retLabels.add(label);
    }

    return retLabels;
  }

  public List<DocumentCategoryKey> getDocumentCategory() {
    List<DocumentCategoryKey> retCategory = this.sharedDoc
        .getDocumentInstance().getDocument().getCategories();
    if (retCategory == null || retCategory.size() <= 0) {
      retCategory = new ArrayList<DocumentCategoryKey>();
      DocumentCategoryKey category = new DocumentCategoryKey(
          this.sharedDoc.getSharer(), "No category defined");
      retCategory.add(category);
    }

    return retCategory;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getNumberParticipants() {
    if (this.participants != null) {
      return String.valueOf(this.participants.size());
    }

    return String.valueOf(0);
  }

  public boolean getBookmarked() {
    return this.bookmarked;
  }

  public List<CollaborationWorkspaceParticipationDto> getParticipants() {
    return participants;
  }

  public void setParticipants(
      List<CollaborationWorkspaceParticipationDto> participants) {
    this.participants = participants;
  }

  public MemberInfoDto getMemberInfo() {
    return memberInfo;
  }

  public void setMemberInfo(MemberInfoDto memberInfo) {
    this.memberInfo = memberInfo;
  }

  public List<CommentAndRepliesDto<SharedDocumentKey>> getWallComments() {
    return wallComments;
  }

  public List<DocumentPageModel> getPages() {
    return pages;
  }

  public SharedDocumentAndStatisticsDto getSharedDocStatistics() {
    return sharedDocStatistics;
  }

  public DocumentPageModel getSelectedPage() {
    return selectedPage;
  }

  public void setSelectedPage(DocumentPageModel selectedPage) {
    this.selectedPage = selectedPage;
  }

  public String getLayoutMode() {
    return layoutMode;
  }

  public void setLayoutMode(String layoutMode) {
    this.layoutMode = layoutMode;
  }

  public String getPageComment() {
    return pageComment;
  }

  public void setPageComment(String pageComment) {
    this.pageComment = pageComment;
  }

  public String getWallComment() {
    return wallComment;
  }

  public void setWallComment(String wallComment) {
    this.wallComment = wallComment;
  }

  public CollaborationWorkspaceDto getWorkspace() {
    return workspace;
  }

  public void setWorkspace(CollaborationWorkspaceDto workspace) {
    this.workspace = workspace;
  }

  public SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getSharedDoc() {
    return sharedDoc;
  }

  public void setSharedDoc(
      SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto sharedDoc) {
    this.sharedDoc = sharedDoc;
  }

  public void shareDocumentExternal() {

    if (StringUtils.isBlank(header) || StringUtils.isBlank(message)
        || StringUtils.isBlank(email) || StringUtils.isBlank(this.firstName)
        || StringUtils.isBlank(this.lastName)) {

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
              "Please provide all the required parameters " + firstName + " "
                  + lastName + " with email " + email));
      return;

    }

    NewExternalSharedDocumentDto externalDocumentShare = new NewExternalSharedDocumentDto();
    externalDocumentShare.setHeader(header);
    externalDocumentShare.setMessage(message);
    externalDocumentShare.setSharer(viewerKey);

    List<ExternalSocialPersonDto> shareList = new ArrayList<ExternalSocialPersonDto>();
    ExternalSocialPersonDto sharedTo = new ExternalSocialPersonDto(this.email,
        this.mobilePhone, this.mobilePhone, this.firstName, this.lastName);
    shareList.add(sharedTo);
    externalDocumentShare.setShareList(shareList);

    log.info("Sharing the document with external party "
        + externalDocumentShare);

    try {
      this.getCollabDocumentShareService().share(sharedDoc.getKey(),
          externalDocumentShare);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Successfully shared document with " + firstName + " " + lastName
                  + " with email " + email));

      this.firstName = StringUtils.EMPTY;
      this.lastName = StringUtils.EMPTY;
      this.email = StringUtils.EMPTY;
      this.message = StringUtils.EMPTY;
      this.header = StringUtils.EMPTY;
      this.mobilePhone = StringUtils.EMPTY;

    } catch (Exception exp) {
      log.error("Error ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error sharing document "));
    }

  }

  public void postWallComment() {
    log.info("Posting doc wall comment " + this.wallComment);

    NewSharedDocumentCommentDto commentDto = new NewSharedDocumentCommentDto(
        viewerKey, this.wallComment, this.sharedDoc.getKey());

    try {
      this.getCollabDocumentShareService().post(commentDto);
    } catch (Exception exp) {
      log.error("Error ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error posting comment."));
    }

    this.wallComments = commentService.getComments(this.sharedDoc.getKey());

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment Saved",
            "Your comment has been saved."));

    this.wallComment = StringUtils.EMPTY;
  }

  public void postWallCommentReply(CommentKey<SharedDocumentKey> commentKey) {

    log.info("Posting doc wall comment " + this.wallComment);

    NewCommentReplyDto commentReply = new NewCommentReplyDto(
        getMemberKey(this.memberInfo), this.pageComment);
    CommentReplyDto<SharedDocumentKey> commentReplyDto = commentService
        .postReply(commentKey, commentReply);
    for (CommentAndRepliesDto<SharedDocumentKey> commentAndReply : wallComments) {
      if (commentAndReply.getKey().equals(commentKey)) {
        commentAndReply.getReplies().add(commentReplyDto);
      }
    }

    this.pageComment = StringUtils.EMPTY;

  }

  public void postPageComment() {
    NewCommentDto commentDto = new NewCommentDto(getMemberKey(this.memberInfo),
        this.pageComment);
    commentService
        .postComment(this.selectedPage.getData().getKey(), commentDto);

    selectedPage.setComments(commentService.getComments(selectedPage.getData()
        .getKey()));

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment Saved",
            "Your comment has been saved."));
    this.pageComment = StringUtils.EMPTY;
  }

  public StreamedContent getDownloadDocument() {
    InputStream inputStream = null;
    try {
      SocialActorKey downloader = this.getMemberKey(memberInfo);
      AssetStream assetStream = this.getCollabDocumentShareService().download(
          downloader, sharedDoc.getKey());
      inputStream = assetStream.getInput();

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Download document",
              "The selected document will begin downloading shortly."));

      return new DefaultStreamedContent(inputStream,
          "application/octet-stream", assetStream.getInfo().getFilename());
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Download document",
              "Unable to download document."));
      log.error("Unable to download document", e);

    } finally {
      // Dont close input stream as PF needs it after the call to do the
      // transfer.
      // IOUtils.closeQuietly(inputStream);
    }
    return null;
  }

  public void nextPage() {
    DocumentInstanceAndDocumentAndViewerElementsDto document = sharedDoc
        .getDocumentInstance();
    if (this.selectedPage != null && document.getPages().size() > 1) {

      /* get the current page index */
      int selectIndex = pages.indexOf(this.selectedPage);

      /* if the current page index is last u have to move to 1st one */
      if (selectIndex == (pages.size() - 1)) {
        this.selectedPage = pages.get(0);
      } else {
        this.selectedPage = pages.get(selectIndex + 1);
      }
    }
  }

  public void prevPage() {
    DocumentInstanceAndDocumentAndViewerElementsDto document = sharedDoc
        .getDocumentInstance();
    if (this.selectedPage != null && document.getPages().size() > 1) {
      /* get the current page index */
      int selectIndex = pages.indexOf(this.selectedPage);

      /* if it is the 1st page then move to the last one */
      if (selectIndex == 0) {
        this.selectedPage = pages.get(document.getPages().size() - 1);
      } else {
        this.selectedPage = pages.get(selectIndex - 1);
      }
    }
  }

  public void shareDocumentWithOtherSpace() {
    log.info("Sharing document " + this.sharedDoc.getKey()
        + " with workspaces " + this.selectedWorkspaces);

    if (this.selectedWorkspaces != null && this.sharedDoc != null) {
      for (CollaborationWorkspaceDto shareSpace : this.selectedWorkspaces) {

        SharedDocumentKey sharedDocKey = new SharedDocumentKey(
            this.workspace.getKey(), this.sharedDoc.getDocumentInstance()
                .getDocument().getKey());
        NewDocumentSharedWithOtherWorkspaceDto shareDoc = new NewDocumentSharedWithOtherWorkspaceDto(
            this.getMemberKey(memberInfo), shareSpace.getKey());
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

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Status",
            "Thanks for sharing the document with other workspaces"));
  }

  public String unshare() {
    try {
      SharedDocumentKey sharedDocKey = new SharedDocumentKey(
          this.workspace.getKey(), this.sharedDoc.getDocumentInstance()
              .getDocument().getKey());
      this.getCollabDocumentShareService().unshare(sharedDocKey,
          this.getMemberKey(memberInfo));

    } catch (Exception exp) {
      log.error("Cannot unshare document", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot unshare the the document from workspace "
                  + exp.getMessage()));
    }

    return "result";
  }

  public void bookmark() {
    log.info("Bookmark the document ");
    try {

      StringBuilder bookMarkName = new StringBuilder()
          .append(this.sharedDoc.getKey().getDocument().getName())
          .append(" in collaboration workspace ")
          .append(this.sharedDoc.getKey().getWorkspace().getName());

      NewBookmarkDto newBookmarkDto = new NewBookmarkDto(viewerKey,
          (IBookmarkableKey) this.sharedDoc.getKey(), bookMarkName.toString());

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

  public String generateCrocodocToken() throws CrocodocException {

    return this.generateCrocodocTokenEditable(sharedDoc.getDocumentInstance(),
        this.memberInfo);

  }

  public String generateBoxToken() throws IOException {
    return this.generateBoxToken(sharedDoc.getDocumentInstance());
  }
}
