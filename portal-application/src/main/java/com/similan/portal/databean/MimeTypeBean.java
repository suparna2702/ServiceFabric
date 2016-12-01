package com.similan.portal.databean;

import java.net.URLEncoder;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.similan.portal.model.DocumentInstanceAndDocumentAndViewerElementsModel;
import com.similan.portal.model.DocumentInstanceAndDocumentModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;

@ManagedBean(name = "mimeTypeBean")
@ApplicationScoped
@Slf4j
public class MimeTypeBean extends BaseView {

  private static final long serialVersionUID = 1L;

  public String getMimeTypeIcon(String mimeType) {
    log.warn("Getting icon for " + mimeType);

    if (mimeType.toLowerCase().startsWith("image"))
      return "page_white_picture48.gif";
    if ("application/pdf".equalsIgnoreCase(mimeType))
      return "page_white_acrobat48.gif";
    if ("application/msword".equalsIgnoreCase(mimeType))
      return "word48.gif";
    if ("application/vnd.ms-excel".equalsIgnoreCase(mimeType))
      return "excel48.gif";
    if ("application/vnd.ms-powerpoint".equalsIgnoreCase(mimeType))
      return "powerpoint48.gif";
    if ("Folder".equalsIgnoreCase(mimeType)
        || "application/vnd.google-apps.folder".equalsIgnoreCase(mimeType))
      return "folder48.gif";
    return "documentlist48.png";
  }

  public String getAssetMediaTypeIconFromModel(
      DocumentInstanceAndDocumentAndViewerElementsModel documentModel) {
    DocumentInstanceAndDocumentAndViewerElementsDto documentInstance = documentModel
        .getData();

    if (documentInstance.getHasIcon()) {
      return "/spring/documenticon/" + documentModel.getPageStorageKey();
    }
    AssetMediaType type = documentInstance.getDetectedInfo().getMediaType();
    return "/images/mimetype/" + getMimeTypeIcon(type.getDescriptor());
  }

  public String getPageStorageKey(DocumentInstanceAndDocumentDto data) {
    // {workspaceOwnerName}/{workspaceName}/{documentName}/{documentInstanceVersion}/{number}",
    // method = RequestMethod.GET
    String documentStorageKey = StringUtils.EMPTY;
    if (StringUtils.isBlank(documentStorageKey)) {
      try {
        StringBuilder builder = new StringBuilder();
        builder
            .append(
                data.getKey().getDocument().getWorkspace().getOwner().getName())
            .append('/')
            .append(
                URLEncoder.encode(data.getKey().getDocument().getWorkspace()
                    .getName(), "ISO-8859-1"))
            .append('/')
            .append(
                URLEncoder.encode(data.getKey().getDocument().getName(),
                    "ISO-8859-1")).append('/')
            .append(data.getKey().getVersion());

        documentStorageKey = builder.toString();
      } catch (Exception e) {
        documentStorageKey = "";
      }
    }
    return documentStorageKey;
  }

  public String getAssetMediaTypeIcon(
      DocumentInstanceAndDocumentDto documentInstance) {

    if (documentInstance.getHasIcon()) {
      return "/spring/documenticon/" + this.getPageStorageKey(documentInstance);
    }
    AssetMediaType type = documentInstance.getDetectedInfo().getMediaType();
    return "/images/mimetype/" + getMimeTypeIcon(type.getDescriptor());
  }

  public String getAssetMediaTypeIcon(
      DocumentInstanceAndDocumentModel documentModel) {
    DocumentInstanceAndDocumentDto documentInstance = documentModel.getData();
    return this.getAssetMediaTypeIcon(documentInstance);
  }

}
