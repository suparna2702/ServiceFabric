package com.similan.portal.view.contentspace;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.AttachmentBuilder;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.crocodoc.CrocodocException;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.model.DocumentInstanceAndDocumentAndViewerElementsModel;
import com.similan.portal.model.DocumentPageModel;
import com.similan.portal.model.SelectableContact;
import com.similan.portal.model.SelectableContactModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.bookmark.dto.key.IBookmarkableKey;
import com.similan.service.api.bookmark.dto.operation.NewBookmarkDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInNetworkSharedDocumentDto;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentPageDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.DocumentInfoUpdateDto;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.DocumentHistoryFilter;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;

@Scope("view")
@Component("docWorkspaceDocumentDetailView")
@Slf4j
public class DocumentWorkspaceDocumentDetailView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo = null;

  private SocialActorKey requestorKey;

  private DocumentInstanceAndDocumentAndViewerElementsModel document = null;

  private DocumentPageModel selectedPage = null;

  private String comment = StringUtils.EMPTY;

  private String layoutMode = BaseView.LAYOUT_MODE_THUMBNAIL;

  private DocumentStatisticsDto docStatistics = null;

  private List<DocumentPageModel> pages;

  private List<WallEntryDto<IWallContainerKey>> docWallEntryList = null;

  private ManagementWorkspaceKey managementWorkspaceKey = null;

  private boolean bookmarked = false;

  private SelectableContactModel contacts;

  private SelectableContact[] selectedContacts;

  private List<CommentAndRepliesDto<DocumentKey>> wallComments = null;

  private NewInNetworkSharedDocumentDto inNetworkShare = new NewInNetworkSharedDocumentDto();

  private String documentName = StringUtils.EMPTY;

  private String documentDescription = StringUtils.EMPTY;

  @PostConstruct
  public void init() {

    String workspaceName = this.getContextParam("msname");
    String documentName = this.getContextParam("dname");

    log.info("Initializing doc detail view managementspace name "
        + workspaceName + " document name " + documentName);

    if (workspaceName != null && documentName != null) {
      managementWorkspaceKey = new ManagementWorkspaceKey(
          getOrgKey(this.orgInfo), workspaceName);
      DocumentKey documentKey = new DocumentKey(managementWorkspaceKey,
          documentName);
      requestorKey = this.getMemberKey(member);

      document = new DocumentInstanceAndDocumentAndViewerElementsModel(this
          .getManagementSpaceService().getDocument(managementWorkspaceKey,
              documentKey, requestorKey));

      docStatistics = this.getDocumentService().getStatistics(documentKey);

      document.setCheckedOut(isCheckedOut(document));
      document.setCheckedOutByUser(isCheckedOutByUser(document));
      bookmarked = this.getBookmarkService().isBookmarked(requestorKey,
          (IBookmarkableKey) documentKey);

      this.wallComments = this.commentService.getComments(document.getKey()
          .getDocument());

      this.documentName = document.getName();
      this.documentDescription = document.getData().getDocument()
          .getDescription();

      pages = new ArrayList<DocumentPageModel>();
      if (document.getData().getPages() != null
          && document.getData().getPages().size() > 0) {
        for (DocumentPageDto dto : document.getData().getPages()) {
          pages.add(new DocumentPageModel(dto));
        }

        log.info("Pages found with count:"
            + document.getData().getPages().size());
        changeToPage(pages.get(0));
      } else {

      }
    }

    /* get the contacts */
    List<SelectableContact> memberContact = this.getContacts(member, orgInfo);
    contacts = new SelectableContactModel(memberContact);
  }

  public String getDocumentName() {
    return documentName;
  }

  public void setDocumentName(String documentName) {
    this.documentName = documentName;
  }

  public String getDocumentDescription() {
    return documentDescription;
  }

  public void setDocumentDescription(String documentDescription) {
    this.documentDescription = documentDescription;
  }

  public List<CommentAndRepliesDto<DocumentKey>> getWallComments() {
    return wallComments;
  }

  public void setWallComments(
      List<CommentAndRepliesDto<DocumentKey>> wallComments) {
    this.wallComments = wallComments;
  }

  public NewInNetworkSharedDocumentDto getInNetworkShare() {
    return inNetworkShare;
  }

  public void setInNetworkShare(NewInNetworkSharedDocumentDto inNetworkShare) {
    this.inNetworkShare = inNetworkShare;
  }

  public boolean getBookmarked() {
    return this.bookmarked;
  }

  public String getMemberId() {
    return this.member.getId().toString();
  }

  public String getMemberName() {
    return this.member.getFirstName();
  }

  public String getDocumentUserId() {
    return new String(getMemberId() + "," + getMemberName());
  }

  public SelectableContactModel getContacts() {
    return contacts;
  }

  public void setContacts(SelectableContactModel contacts) {
    this.contacts = contacts;
  }

  public SelectableContact[] getSelectedContacts() {
    return selectedContacts;
  }

  public MemberInfoDto getMember() {
    return member;
  }

  public void setSelectedContacts(SelectableContact[] selectedContacts) {
    this.selectedContacts = selectedContacts;
  }

  public List<WallEntryDto<IWallContainerKey>> getDocWallEntryList() {
    if (docWallEntryList == null) {
      log.info("Document key " + document.getData().getDocument().getKey());
      docWallEntryList = this.getWallService().geDocumentHistory(
          document.getData().getDocument().getKey(), DocumentHistoryFilter.ALL);
    }
    return docWallEntryList;
  }

  public void setDocWallEntryList(
      List<WallEntryDto<IWallContainerKey>> docWallEntryList) {
    this.docWallEntryList = docWallEntryList;
  }

  public void updateDocumentInfo() {

    if (StringUtils.isBlank(this.documentName)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Document name cannot be empty. Please provide a valid name "));
      return;
    }

    if (StringUtils.isBlank(this.documentDescription)) {
      FacesContext
          .getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",
                  "Document description is empty. Retaining the previous description "));
      this.documentDescription = this.document.getData().getDocument()
          .getDescription();
    }

    DocumentInfoUpdateDto updateInfo = new DocumentInfoUpdateDto();
    updateInfo.setName(this.documentName);
    updateInfo.setDescription(this.documentDescription);

    try {
      DocumentInstanceAndDocumentAndViewerElementsDto data = this
          .getManagementSpaceService().updateDocumentInfo(
              this.managementWorkspaceKey,
              this.document.getData().getDocument().getKey(),
              this.requestorKey, updateInfo);
      this.document.setData(data);

      this.documentName = document.getName();
      this.documentDescription = document.getData().getDocument()
          .getDescription();

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Updated document information successfully "));
    } catch (Exception exp) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Could not update document due to " + exp.getMessage()));
    }

  }

  public void share() {
    log.info("Share document with partners " + this.document.getName());

    if (this.selectedContacts != null) {
      List<SocialActorKey> sharedTo = new ArrayList<SocialActorKey>();

      for (SelectableContact contact : this.selectedContacts) {
        SocialActorKey contactActorKey = contact.getContact().getContact()
            .getKey();
        log.info("Contact actor key " + contactActorKey);
        sharedTo.add(contactActorKey);
        this.inNetworkShare.setSharedTo(sharedTo);
        this.inNetworkShare.setWorkspaceKey(this.managementWorkspaceKey);
        this.inNetworkShare.setSharer(this.requestorKey);

        try {
          this.getManagementSpaceService().share(
              this.document.getKey().getDocument(), this.inNetworkShare);
        } catch (Exception exp) {
          log.error("Error sharing the document ", exp);
          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  "Could not share document because " + exp.getMessage()));
        }

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                "Sucessfully shared document "));
        this.inNetworkShare = new NewInNetworkSharedDocumentDto();

      }
    }
  }

  private boolean isCheckedOut(
      DocumentInstanceAndDocumentAndViewerElementsModel doc) {
    if (doc == null)
      return false;
    SocialActorKey key = documentService.getLockOwner(doc.getData()
        .getDocument().getKey());
    return key != null;
  }

  private boolean isCheckedOutByUser(
      DocumentInstanceAndDocumentAndViewerElementsModel doc) {
    if (doc == null)
      return false;
    SocialActorKey key = documentService.getLockOwner(doc.getData()
        .getDocument().getKey());
    if (key == null)
      return false;
    return key.equals(getMemberKey(this.member));
  }

  private void changeToPage(DocumentPageModel page) {
    page.setComments(commentService.getComments(page.getData().getKey()));
    setSelectedPage(page);
  }

  public DocumentStatisticsDto getDocStatistics() {
    return docStatistics;
  }

  public void setDocStatistics(DocumentStatisticsDto docStatistics) {
    this.docStatistics = docStatistics;
  }

  public String getLayoutMode() {
    return layoutMode;
  }

  public void setLayoutMode(String layoutMode) {
    this.layoutMode = layoutMode;
  }

  public DocumentInstanceAndDocumentAndViewerElementsModel getDocument() {
    return document;
  }

  public void setDocument(
      DocumentInstanceAndDocumentAndViewerElementsModel document) {
    this.document = document;
  }

  public void checkOutDocument() {

    try {
      boolean result = this.getDocumentService().lock(
          document.getData().getDocument().getKey(), getMemberKey(this.member));

      this.getManagementSpaceService().checkOutDocument(
          this.managementWorkspaceKey, getMemberKey(this.member),
          document.getData().getDocument().getKey());

      if (result == true) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Checked out",
                "You have checked out "
                    + document.getData().getDocument().getKey().getName()));
        document.setCheckedOut(true);
        document.setCheckedOutByUser(true);

        /*
         * String redirectUrl = this.managementWorkspaceDocumentView(
         * "docspace/viewDocumentDetails.xhtml", document.getData()
         * .getDocument().getKey());
         * FacesContext.getCurrentInstance().getExternalContext()
         * .redirect(redirectUrl);
         */

      } else {
        FacesContext
            .getCurrentInstance()
            .addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checked out",
                    "Could not check out document because it is already checked out."));
      }
    } catch (Exception exp) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Could not check out document because " + exp.getMessage()));
    }

  }

  public void checkInDocument(FileUploadEvent event) {
    UploadedFile uploadedFile = event.getFile();

    // cannot checkin null document
    if (uploadedFile == null) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot not check in document since it is NULL."));
      return;
    }

    // cannot checkin file with blank name. The file name can be
    // different for now
    String fileName = uploadedFile.getFileName();

    if (StringUtils.isBlank(fileName)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot not check in a file with empty name. "
                  + "Please provide a valid name"));
      return;
    }

    try {
      // Now create a document
      AssetMetadataDto metadata = new AssetMetadataDto(
          uploadedFile.getContentType(), null);

      InputStream fis = uploadedFile.getInputstream();
      new AttachmentBuilder().mediaType(uploadedFile.getContentType()).object(
          fis);

      NewAssetStream assetStream = new NewAssetStream(metadata,
          fis);
      NewDocumentInstanceDto checkInDto = new NewDocumentInstanceDto(
          this.getMemberKey(member), fileName);

      boolean result = this.getManagementSpaceService().checkInDocument(
          this.managementWorkspaceKey, this.document.getKey().getDocument(),
          assetStream, checkInDto);

      this.document = new DocumentInstanceAndDocumentAndViewerElementsModel(
          this.getManagementSpaceService().getDocument(managementWorkspaceKey,
              this.document.getKey().getDocument(), this.getMemberKey(member)));

      if (result == true) {
        document.setCheckedOut(false);
        document.setCheckedOutByUser(false);
      }

      String redirectUrl = this.managementWorkspaceDocumentView(
          "docspace/viewDocumentDetails.xhtml", document.getData()
              .getDocument().getKey());
      log.info("Redirection URL " + redirectUrl);

      FacesContext.getCurrentInstance().getExternalContext()
          .redirect(redirectUrl);

    } catch (Exception exp) {
      log.error("Error checking in ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Could not check in document since " + exp.getMessage()));
    }

  }

  public void postWallComment() {

    try {
      NewCommentDto commentDto = new NewCommentDto(getMemberKey(this.member),
          comment);
      commentService.postComment(document.getKey().getDocument(), commentDto);
      this.wallComments = this.commentService.getComments(document.getKey()
          .getDocument());

      comment = StringUtils.EMPTY;

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Your comment has been saved."));

    } catch (Exception exp) {
      log.error("Error creating page comment", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error posting comment" + exp.getMessage()));

    }
  }

  public DocumentPageModel getSelectedPage() {
    return selectedPage;
  }

  public void setSelectedPage(DocumentPageModel selectedPage) {
    this.selectedPage = selectedPage;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public StreamedContent getDownloadDocument() {
    InputStream inputStream = null;
    try {
      log.warn(document.toString());
      SocialActorKey downloaderKey = this.getMemberKey(member);
      AssetStream assetStream = this.getManagementSpaceService().download(
          managementWorkspaceKey, downloaderKey,
          document.getKey().getDocument());

      inputStream = assetStream.getInput();
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Download document",
              "The selected document will begin downloading shortly."));

      return new DefaultStreamedContent(inputStream,
          "application/octet-stream", document.getData().getDetectedInfo()
              .getFilename());
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
    if (this.selectedPage != null && document.getData().getPages().size() > 1) {

      /* get the current page index */
      int selectIndex = document.getData().getPages()
          .indexOf(this.selectedPage);

      /* if the current page index is last u have to move to 1st one */
      if (selectIndex == (pages.size() - 1)) {
        changeToPage(pages.get(0));
      } else {
        changeToPage(pages.get(selectIndex + 1));
      }
    }
  }

  public void prevPage() {
    if (this.selectedPage != null && document.getData().getPages().size() > 1) {
      /* get the current page index */
      int selectIndex = document.getData().getPages()
          .indexOf(this.selectedPage);

      /* if it is the 1st page then move to the last one */
      if (selectIndex == 0) {
        changeToPage(pages.get(document.getData().getPages().size() - 1));
      } else {
        changeToPage(pages.get(selectIndex - 1));
      }
    }
  }

  public List<DocumentPageModel> getPages() {
    return pages;
  }

  public void bookmark() {
    log.info("Bookmark the document ");
    try {

      StringBuilder bookMarkName = new StringBuilder()
          .append(this.document.getKey().getDocument().getName())
          .append(" in content workspace ")
          .append(this.document.getKey().getDocument().getWorkspace().getName());

      NewBookmarkDto newBookmarkDto = new NewBookmarkDto(
          this.getMemberKey(member), (IBookmarkableKey) this.document.getKey()
              .getDocument(), bookMarkName.toString());

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
    return this.generateCrocodocTokenEditable(document.getData(), member);
  }

  public String generateBoxToken() throws IOException {
    return this.generateBoxToken(document.getData());
  }
}
