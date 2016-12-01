package com.similan.portal.view.collabspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentAndStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentCategoryDto;

@Slf4j
public class CollabWorkspaceDocumentViewUtil extends BaseView {

  private static final long serialVersionUID = 1L;

  private List<SharedDocumentAndStatisticsDto> viewableWorkspaceDocs = new ArrayList<SharedDocumentAndStatisticsDto>();

  private List<SharedDocumentAndStatisticsDto> workspaceDocs = new ArrayList<SharedDocumentAndStatisticsDto>();

  private Map<String, List<SharedDocumentAndStatisticsDto>> categoryDocumentMap = new HashMap<String, List<SharedDocumentAndStatisticsDto>>();

  private String documentSearchText = StringUtils.EMPTY;

  public Map<String, List<SharedDocumentAndStatisticsDto>> getCategoryDocumentMap() {
    return categoryDocumentMap;
  }

  public void setCategoryDocumentMap(
      Map<String, List<SharedDocumentAndStatisticsDto>> categoryDocumentMap) {
    this.categoryDocumentMap = categoryDocumentMap;
  }

  public List<SharedDocumentAndStatisticsDto> getViewableWorkspaceDocs() {
    return viewableWorkspaceDocs;
  }

  public void setViewableWorkspaceDocs(
      List<SharedDocumentAndStatisticsDto> viewableWorkspaceDocs) {
    this.viewableWorkspaceDocs = viewableWorkspaceDocs;
  }

  public List<SharedDocumentAndStatisticsDto> getWorkspaceDocs() {
    return workspaceDocs;
  }

  public void setWorkspaceDocs(
      List<SharedDocumentAndStatisticsDto> workspaceDocs) {
    this.workspaceDocs = workspaceDocs;
  }

  public String getDocumentSearchText() {
    return documentSearchText;
  }

  public void setDocumentSearchText(String documentSearchText) {
    this.documentSearchText = documentSearchText;
  }

  public int getNumberSharedUnreadDocuments() {
    if (workspaceDocs != null) {
      return workspaceDocs.size();
    } else {
      return 0;
    }
  }

  public List<String> getCategoryList() {
    List<String> categoryList = new ArrayList<String>();

    for (String category : categoryDocumentMap.keySet()) {
      categoryList.add(category);
    }

    return categoryList;
  }

  public void getSharedDocs(CollaborationWorkspaceKey workspaceKey,
      SocialActorKey viewerKey) {
    List<SharedDocumentAndStatisticsDto> allSharedDocs = this
        .getCollabDocumentShareService().getSharedDocumentAndStatistics(
            workspaceKey, viewerKey);

    for (SharedDocumentAndStatisticsDto sharedDoc : allSharedDocs) {
      if (sharedDoc.getSharedDocument().getUnshared().booleanValue() == false) {
        this.workspaceDocs.add(sharedDoc);
      }
    }

    this.viewableWorkspaceDocs = this.workspaceDocs;

    /* populate the category map */
    if (this.workspaceDocs != null) {

      /* all categories */
      this.categoryDocumentMap.put("All", this.workspaceDocs);

      for (SharedDocumentAndStatisticsDto doc : this.workspaceDocs) {
        List<DocumentCategoryDto> docCategoryList = this
            .getDocumentCategoryService().getFor(doc.getKey().getDocument());

        for (DocumentCategoryDto category : docCategoryList) {

          if (this.categoryDocumentMap.containsKey(category.getKey().getName())) {
            List<SharedDocumentAndStatisticsDto> docList = this.categoryDocumentMap
                .get(category.getKey().getName());
            docList.add(doc);
          } else {
            List<SharedDocumentAndStatisticsDto> docList = new ArrayList<SharedDocumentAndStatisticsDto>();
            docList.add(doc);
            this.categoryDocumentMap.put(category.getKey().getName(), docList);
          }
        }
      }
    }
  }

  public void filterByCategory(String category) {

    log.info("The category filter " + category);

    if (StringUtils.isNotBlank(category)) {

      if (category.equalsIgnoreCase("All")) {
        this.viewableWorkspaceDocs = this.workspaceDocs;
        return;
      }

      List<SharedDocumentAndStatisticsDto> docList = categoryDocumentMap
          .get(category);
      if (docList != null) {
        this.viewableWorkspaceDocs = docList;
      }
    }
  }

  public void documentSearch() {

    log.info("Search string " + this.documentSearchText);

    if (StringUtils.isBlank(this.documentSearchText) != true) {
      this.viewableWorkspaceDocs = new ArrayList<SharedDocumentAndStatisticsDto>();
      for (SharedDocumentAndStatisticsDto doc : this.workspaceDocs) {

        String documentTitle = StringUtils.lowerCase(doc.getSharedDocument()
            .getKey().getDocument().getName());

        if (documentTitle.contains(StringUtils
            .lowerCase(this.documentSearchText))) {
          this.viewableWorkspaceDocs.add(doc);
        }
      }
    } else {
      this.viewableWorkspaceDocs = this.workspaceDocs;
    }
  }

}
