package com.similan.portal.view.contentspace;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.model.DocumentCategoryModel;
import com.similan.portal.model.DocumentInstanceAndDocumentModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentCategoryDto;
import com.similan.service.api.document.dto.basic.DocumentLabelDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentLabelAndDecendantsDto;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.document.dto.operation.NewDocumentCategoryDto;
import com.similan.service.api.document.dto.operation.NewDocumentLabelDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceParticipationKey;

@Scope("view")
@Component("docWorkspaceView")
@Slf4j
public class DocumentWorkspaceView extends BaseView {

  private static final long serialVersionUID = -352543563861403700L;

  private static final String FILTER_MODE_CATEGORY = "CATEGORY";

  private static final DocumentCategoryKey ALL_CATEGORY_KEY = new DocumentCategoryKey(
      "-1", "All");
  private static final DocumentCategoryKey UNSPECIFIED_CATEGORY_KEY = new DocumentCategoryKey(
      "-1", "Unspecified");

  private TreeNode allLabels = null;
  private TreeNode selectedLabel = null;
  private String labelToAdd = null;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo = null;

  private String documentSearchText = StringUtils.EMPTY;
  private List<DocumentInstanceAndDocumentModel> documents = null;
  private List<DocumentInstanceAndDocumentModel> allWorkspaceDocuments = null;

  private DocumentCategoryModel selectedFilterCategory = null;
  private List<DocumentCategoryModel> allCategories = null;
  private String filterMode = FILTER_MODE_CATEGORY;
  private TreeNode selectedFilterLabelNode = null;
  private DocumentLabelDto selectedFilterLabel = null;
  private DocumentInstanceAndDocumentModel documentToCheckOutOrIn = null;
  private DocumentInstanceAndDocumentModel documentToDownload = null;
  private String layoutMode = LAYOUT_MODE_LIST;
  private DocumentInstanceAndDocumentModel selectedDocumentForEdit = null;
  private String categoryToAdd = null;
  private TreeNode[] selectedDocumentLabels;
  private List<ActorDto> perticipants = null;
  private Map<String, ActorDto> perticipantsMap = new HashMap<String, ActorDto>();

  private ManagementWorkspaceKey workspaceKey;

  @PostConstruct
  public void init() {
    // get the necessary params to create
    // management workspace key if not go to default space
    String workspaceName = this.getContextParam("msname");

    if (StringUtils.isBlank(workspaceName) != true) {
      workspaceKey = new ManagementWorkspaceKey(getOrgKey(this.orgInfo),
          workspaceName);
      log.info("Management workspace key " + workspaceKey);
    } else {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "No Workspace Defined "));
      try {
        FacesContext.getCurrentInstance().getExternalContext()
            .redirect("/member/myHome.xhtml");
      } catch (Exception exp) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "Redirection failed "));
        return;
      }

    }

    perticipants = this.getManagementSpaceService().getParticipants(
        workspaceKey);
    // populate the map
    for (ActorDto pertDto : perticipants) {
      perticipantsMap.put(pertDto.getKey().getName(), pertDto);
    }

    List<DocumentInstanceAndDocumentDto> results = new ArrayList<DocumentInstanceAndDocumentDto>();
    results = managementSpaceService.getDocuments(this.workspaceKey);

    this.allWorkspaceDocuments = new ArrayList<DocumentInstanceAndDocumentModel>();

    for (DocumentInstanceAndDocumentDto dto : results) {
      DocumentInstanceAndDocumentModel model = new DocumentInstanceAndDocumentModel(
          dto);

      // get the statistics
      DocumentStatisticsDto stats = this.getDocumentService().getStatistics(
          dto.getDocument().getKey());
      model.setStatistics(stats);
      model.setCheckedOut(isCheckedOut(dto));
      model.setCheckedOutByUser(isCheckedOutByThisUser(dto));
      log.info("Model: " + model);
      this.allWorkspaceDocuments.add(model);

    }

    this.documents = this.allWorkspaceDocuments;
  }

  public void addLabel() {

    try {
      if (labelToAdd != null) {
        if (selectedLabel != null) {
          documentLabelService.create(new DocumentLabelKey(
              getOrgKey(this.orgInfo), labelToAdd), new NewDocumentLabelDto(
              ((DocumentLabelDto) selectedLabel.getData()).getKey()));
        } else {
          documentLabelService.create(new DocumentLabelKey(
              getOrgKey(this.orgInfo), labelToAdd), new NewDocumentLabelDto(
              null));
        }
        labelToAdd = null;
        allLabels = null;
      }
    } catch (Exception exp) {
      log.error("Cannot add a category", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot create category label : " + exp.getMessage()));
    }

  }

  public void addCategory() {

    try {
      if (categoryToAdd != null && !categoryToAdd.equals("")) {

        NewDocumentCategoryDto newCategoryDto = new NewDocumentCategoryDto();
        DocumentCategoryDto category = documentCategoryService
            .create(new DocumentCategoryKey(this.getOrgKey(this.orgInfo),
                categoryToAdd), newCategoryDto);

        allCategories.add(new DocumentCategoryModel(category, true));
        categoryToAdd = null;
      }
    } catch (Exception exp) {
      log.error("Cannot add a category", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot create category because : " + exp.getMessage()));
    }
  }

  public TreeNode getAllLabels() {

    if (allLabels == null) {
      SocialActorKey ownerKey = this.getOrgKey(this.orgInfo);
      List<DocumentLabelAndDecendantsDto> labels = this.documentLabelService
          .getRootForOwner(ownerKey);

      allLabels = new DefaultTreeNode(new DocumentLabelDto(
          new DocumentLabelKey("-1", "-1"), null), null);

      for (DocumentLabelAndDecendantsDto label : labels) {
        createTreeNodeForLabel(label, allLabels);
      }
    }
    return allLabels;
  }

  private TreeNode createTreeNodeForLabel(DocumentLabelAndDecendantsDto label,
      TreeNode parent) {
    TreeNode treeNode = new DefaultTreeNode(label, parent);
    treeNode.setSelectable(true);
    for (DocumentLabelAndDecendantsDto children : label.getChildren()) {
      createTreeNodeForLabel(children, treeNode);
    }
    return treeNode;
  }

  public List<ActorDto> getShareList() {
    List<ActorDto> empList = this.getNetworkService().getEmployee(
        this.getOrgKey(orgInfo));
    List<ActorDto> shareList = new ArrayList<ActorDto>();
    for (ActorDto shareDto : empList) {
      if (this.perticipantsMap.containsKey(shareDto.getKey().getName()) != true) {
        shareList.add(shareDto);
      }
    }

    return shareList;
  }

  public void documentSearch() {
    log.error("Search string" + this.documentSearchText);

    if (StringUtils.isBlank(this.documentSearchText) != true) {
      this.documents = new ArrayList<DocumentInstanceAndDocumentModel>();
      for (DocumentInstanceAndDocumentModel doc : this.allWorkspaceDocuments) {

        String documentTitle = StringUtils.lowerCase(doc.getName());

        if (documentTitle.contains(StringUtils
            .lowerCase(this.documentSearchText))) {
          this.documents.add(doc);
        }
      }
    } else {
      this.documents = this.allWorkspaceDocuments;
    }

    this.documentSearchText = StringUtils.EMPTY;
  }

  public String getDocumentSearchText() {
    return documentSearchText;
  }

  public void setDocumentSearchText(String documentSearchText) {
    this.documentSearchText = documentSearchText;
  }

  public List<ActorDto> getPerticipants() {
    return perticipants;
  }

  public ManagementWorkspaceKey getWorkspaceKey() {
    return workspaceKey;
  }

  public void setWorkspaceKey(ManagementWorkspaceKey workspaceKey) {
    this.workspaceKey = workspaceKey;
  }

  public void setAllLabels(TreeNode allLabels) {
    this.allLabels = allLabels;
  }

  public TreeNode getSelectedLabel() {
    return selectedLabel;
  }

  public void setSelectedLabel(TreeNode selectedLabel) {
    this.selectedLabel = selectedLabel;
  }

  public String getLabelToAdd() {
    return labelToAdd;
  }

  public void setLabelToAdd(String labelToAdd) {
    this.labelToAdd = labelToAdd;
  }

  public List<DocumentInstanceAndDocumentModel> getDocuments() {
    return documents;
  }

  public void setDocuments(List<DocumentInstanceAndDocumentModel> documents) {
    this.documents = documents;
  }

  public DocumentCategoryModel getSelectedFilterCategory() {
    return selectedFilterCategory;
  }

  public void addPerticipant(SocialActorKey perticipant) {
    try {
      ManagementWorkspaceParticipationKey pertKey = this
          .getManagementSpaceService()
          .addPerticipant(workspaceKey, perticipant);

      SocialActorKey actorKey = pertKey.getPerticipant();
      ActorDto dispDto = this.getSocialActorService().getActor(actorKey);
      this.perticipants.add(dispDto);
      this.perticipantsMap.put(actorKey.getName(), dispDto);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Successfully added participant." + dispDto.getDisplayName()
                  + " in contant workspace " + workspaceKey.getName()));

    } catch (Exception exp) {

      log.error("Error adding participants " + exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error adding participant." + exp.getMessage()));
    }
  }

  public List<DocumentCategoryModel> getAllCategories() {

    if (allCategories == null) {
      allCategories = new ArrayList<DocumentCategoryModel>();

      List<DocumentCategoryDto> categories = this.documentCategoryService
          .getForOwner(getOrgKey(this.orgInfo));
      for (DocumentCategoryDto cat : categories) {
        allCategories.add(new DocumentCategoryModel(cat));
      }
    }
    return allCategories;
  }

  public void setSelectedFilterCategory(DocumentCategoryModel category) {
    selectedFilterCategory = category;
    List<DocumentInstanceAndDocumentDto> results = new ArrayList<DocumentInstanceAndDocumentDto>();

    if (selectedFilterCategory == null) {
      results = managementSpaceService.getDocuments(this.workspaceKey);
    } else if (!selectedFilterCategory.getKey().equals(
        DocumentWorkspaceView.UNSPECIFIED_CATEGORY_KEY)
        && !selectedFilterCategory.getKey().equals(
            DocumentWorkspaceView.ALL_CATEGORY_KEY)) {
      results = documentCategoryService.getFor(selectedFilterCategory.getKey());
    } else if (selectedFilterCategory.getKey().equals(
        DocumentWorkspaceView.UNSPECIFIED_CATEGORY_KEY)) {
      // FIXME: Implement call to get uncategorized documents
      // documents = documentService
      // .getAllUncategorizedDocuments(getOrgKey());
      List<DocumentInstanceAndDocumentDto> unFilteredResults = managementSpaceService
          .getDocuments(this.workspaceKey);
      for (DocumentInstanceAndDocumentDto dto : unFilteredResults) {
        if (dto.getDocument().getCategories() == null
            || dto.getDocument().getCategories().size() == 0) {
          results.add(dto);
        }
      }
    } else {
      results = managementSpaceService.getDocuments(this.workspaceKey);
    }

    documents = new ArrayList<DocumentInstanceAndDocumentModel>();

    for (DocumentInstanceAndDocumentDto dto : results) {
      DocumentInstanceAndDocumentModel model = new DocumentInstanceAndDocumentModel(
          dto);

      // get the statistics
      DocumentStatisticsDto stats = this.getDocumentService().getStatistics(
          dto.getDocument().getKey());
      model.setStatistics(stats);
      model.setCheckedOut(isCheckedOut(dto));
      model.setCheckedOutByUser(isCheckedOutByThisUser(dto));
      log.info("Model: " + model);
      documents.add(model);

    }

  }

  public List<DocumentCategoryModel> getAllFilterCategories() {
    List<DocumentCategoryModel> categories = new ArrayList<DocumentCategoryModel>();

    /* add all category */
    DocumentCategoryDto allCat = new DocumentCategoryDto(
        DocumentWorkspaceView.ALL_CATEGORY_KEY);
    categories.add(new DocumentCategoryModel(allCat));

    categories.addAll(getAllCategories());

    DocumentCategoryDto unspecified = new DocumentCategoryDto(
        DocumentWorkspaceView.UNSPECIFIED_CATEGORY_KEY);
    categories.add(new DocumentCategoryModel(unspecified));

    return categories;

  }

  public void setAllCategories(List<DocumentCategoryModel> allCategories) {
    this.allCategories = allCategories;
  }

  private void clearSelectedLabels() {
    if (selectedDocumentLabels == null)
      return;

    for (TreeNode node : selectedDocumentLabels) {
      node.setSelected(false);
    }
    selectedDocumentLabels = null;
  }

  private DocumentInstanceAndDocumentModel getDocument(Long documentId) {

    for (DocumentInstanceAndDocumentModel document : this.allWorkspaceDocuments) {
      log.info("Evaluating Document id " + document.getKey().getId());
      if (document.getKey().getId().equals(documentId)) {
        return document;
      }
    }
    return null;
  }

  public void editDocumentStart(Long documentId) {

    log.info("editDocumentStart Document id to be selected " + documentId);

    DocumentInstanceAndDocumentModel document = this.getDocument(documentId);
    log.info("editDocumentStart found document" + document.getName());

    this.setSelectedDocumentForEdit(document);
    setCategoryToAdd(null);
    clearSelectedLabels();

    if (document.getData().getDocument().getCategories() != null) {
      for (DocumentCategoryModel availableCategory : getAllCategories()) {
        availableCategory.setSelected(false);
        for (DocumentCategoryKey selectedCategory : document.getData()
            .getDocument().getCategories()) {
          if (availableCategory.getKey().equals(selectedCategory)) {
            availableCategory.setSelected(true);
          }
        }
      }
    }

  }

  public void editFileComplete() {
    log.warn("Called: Edit document complete");

    List<DocumentCategoryKey> originalCategoryKeys = new ArrayList<DocumentCategoryKey>(
        selectedDocumentForEdit.getData().getDocument().getCategories());

    List<DocumentCategoryKey> categoriesToDelete = new ArrayList<DocumentCategoryKey>();
    List<DocumentCategoryKey> categoriesToAdd = new ArrayList<DocumentCategoryKey>();

    selectedDocumentForEdit.getData().getDocument().getCategories().clear();
    for (DocumentCategoryModel cat : allCategories) {
      if (cat.isSelected()) {
        selectedDocumentForEdit.getData().getDocument().getCategories()
            .add(cat.getData().getKey());
        cat.setSelected(false);
      }
    }

    selectedDocumentForEdit.getData().getDocument().getLabels().clear();
    if (selectedDocumentLabels != null) {
      for (TreeNode node : selectedDocumentLabels) {
        selectedDocumentForEdit.getData().getDocument().getLabels()
            .add(((DocumentLabelDto) node.getData()).getKey());
      }
    }

    for (DocumentCategoryKey category : originalCategoryKeys) {
      if (!selectedDocumentForEdit.getData().getDocument().getCategories()
          .contains(category))
        categoriesToDelete.add(category);
    }
    for (DocumentCategoryKey category : selectedDocumentForEdit.getData()
        .getDocument().getCategories()) {
      if (!originalCategoryKeys.contains(category))
        categoriesToAdd.add(category);
    }

    for (DocumentCategoryKey category : categoriesToDelete) {
      log.warn("Removing category to document " + category);
      documentCategoryService.remove(category, selectedDocumentForEdit
          .getData().getDocument().getKey());
    }
    for (DocumentCategoryKey category : categoriesToAdd) {
      log.warn("Adding category to document " + category);
      documentCategoryService.add(category, selectedDocumentForEdit.getData()
          .getDocument().getKey());
    }

    categoryToAdd = null;
    selectedDocumentForEdit = null;

  }

  public DocumentInstanceAndDocumentModel getSelectedDocumentForEdit() {
    log.info("Getting selected document is called ");
    return selectedDocumentForEdit;
  }

  public void setSelectedDocumentForEdit(
      DocumentInstanceAndDocumentModel selectedDocumentForEdit) {

    log.info("Selecting document " + selectedDocumentForEdit.getName());
    this.selectedDocumentForEdit = selectedDocumentForEdit;
  }

  public String getCategoryToAdd() {
    return categoryToAdd;
  }

  public void setCategoryToAdd(String categoryToAdd) {
    this.categoryToAdd = categoryToAdd;
  }

  public TreeNode[] getSelectedDocumentLabels() {
    return selectedDocumentLabels;
  }

  public void setSelectedDocumentLabels(TreeNode[] selectedDocumentLabels) {
    this.selectedDocumentLabels = selectedDocumentLabels;
  }

  public String getFilterMode() {
    return filterMode;
  }

  public void setFilterMode(String filterMode) {
    this.filterMode = filterMode;
  }

  public DocumentLabelDto getSelectedFilterLabel() {
    return selectedFilterLabel;
  }

  public void setSelectedFilterLabel(DocumentLabelDto selectedFilterLabel) {
    this.selectedFilterLabel = selectedFilterLabel;
  }

  public void onFilterNodeSelected(NodeSelectEvent event) {
    selectedFilterLabel = (DocumentLabelDto) event.getTreeNode().getData();
    documents = null;
  }

  public void onFilterNodeUnelected(NodeUnselectEvent event) {
    selectedFilterLabel = null;
    documents = null;
  }

  public TreeNode getSelectedFilterLabelNode() {
    return selectedFilterLabelNode;
  }

  public void setSelectedFilterLabelNode(TreeNode selectedFilterLabelNode) {
    this.selectedFilterLabelNode = selectedFilterLabelNode;
  }

  public void checkOutDocument() {
    boolean result = documentService.lock(documentToCheckOutOrIn.getData()
        .getDocument().getKey(), getMemberKey(this.member));
    if (result == true) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Checked out",
              "You have checked out "
                  + documentToCheckOutOrIn.getData().getDocument().getKey()
                      .getName()));
      documentToCheckOutOrIn.setCheckedOut(true);
      documentToCheckOutOrIn.setCheckedOutByUser(true);
    } else {
      FacesContext
          .getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checked out",
                  "Could not check out document because it is already checked out."));
    }
  }

  public void checkInDocument() {
    boolean result = documentService.unlock(documentToCheckOutOrIn.getData()
        .getDocument().getKey(), getMemberKey(this.member));
    if (result == true) {
      documentToCheckOutOrIn.setCheckedOut(false);
      documentToCheckOutOrIn.setCheckedOutByUser(false);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Checked in",
              "You have checked in "
                  + documentToCheckOutOrIn.getData().getDocument().getKey()
                      .getName()));
    } else {
      FacesContext
          .getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checked out",
                  "Could not check in document because it is checked out by another member."));
    }
  }

  private boolean isCheckedOut(DocumentInstanceAndDocumentDto doc) {
    if (doc == null)
      return false;
    SocialActorKey key = documentService.getLockOwner(doc.getDocument()
        .getKey());
    return key != null;
  }

  private boolean isCheckedOutByThisUser(DocumentInstanceAndDocumentDto doc) {
    if (doc == null)
      return false;
    SocialActorKey key = documentService.getLockOwner(doc.getDocument()
        .getKey());
    if (key == null)
      return false;
    return key.equals(getMemberKey(this.member));
  }

  public StreamedContent getDownloadDocument() {
    InputStream inputStream = null;
    try {
      log.warn(documentToDownload.toString());

      AssetStream assetStream = documentInstanceService
          .retrieveOriginal(documentToDownload.getKey());
      inputStream = assetStream.getInput();

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Download document",
              "The selected document will begin downloading shortly."));

      return new DefaultStreamedContent(inputStream,
          "application/octet-stream", documentToDownload.getData()
              .getDocument().getKey().getName());
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

  public DocumentInstanceAndDocumentModel getDocumentToCheckOutOrIn() {
    return documentToCheckOutOrIn;
  }

  public void setDocumentToCheckOutOrIn(
      DocumentInstanceAndDocumentModel documentToCheckOutOrIn) {
    this.documentToCheckOutOrIn = documentToCheckOutOrIn;
  }

  public DocumentInstanceAndDocumentModel getDocumentToDownload() {
    return documentToDownload;
  }

  public void setDocumentToDownload(
      DocumentInstanceAndDocumentModel documentToDownload) {
    this.documentToDownload = documentToDownload;
  }

  public void deleteDocument(DocumentKey documentKey) {
    log.info("Document to be deleted " + documentKey);

    // remove from list

    try {

      // remove from all document list
      for (Iterator<DocumentInstanceAndDocumentModel> iter = this.allWorkspaceDocuments
          .listIterator(); iter.hasNext();) {
        DocumentInstanceAndDocumentModel document = iter.next();
        if (document.getData().getDocument().getKey().getId()
            .equals(documentKey.getId())) {
          iter.remove();
          log.info("Removed from all doc list ");
        }
      }

      for (Iterator<DocumentInstanceAndDocumentModel> iter = this.documents
          .listIterator(); iter.hasNext();) {
        DocumentInstanceAndDocumentModel document = iter.next();
        if (document.getData().getDocument().getKey().getId()
            .equals(documentKey.getId())) {
          iter.remove();
          log.info("Removed from view doc list ");
        }
      }

      // now delete
      this.getManagementSpaceService().delete(workspaceKey,
          this.getMemberKey(member), documentKey);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Successfully deleted document " + documentKey.getName()));

    } catch (Exception exp) {
      log.error("Error deleting document ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error deleting document " + exp.getMessage()));
    }

  }

  public void deleteSelectedFiles() {
    boolean documentsDeleted = false;
    for (DocumentInstanceAndDocumentModel document : getDocuments()) {
      if (document.isSelected()) {
        documentService.delete(document.getData().getDocument().getKey());
        documentsDeleted = true;
      }
    }
    documents = null;
    if (documentsDeleted == true) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete Document",
              "The selected documents have been deleted."));
    }
  }

  public String getLayoutMode() {
    return layoutMode;
  }

  public void setLayoutMode(String layoutMode) {
    this.layoutMode = layoutMode;
  }

}
