package com.similan.service.impl.document.viewer.generic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import lombok.Cleanup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.framework.box.BoxClient;
import com.similan.framework.box.BoxClient.DocumentStatus;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.impl.document.icon.DocumentIconGenerator;
import com.similan.service.impl.document.viewer.AbstractDocumentViewer;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

@Component
public class GenericBoxConverterDocumentViewer extends AbstractDocumentViewer {
  public static final String DOCUMENT_ID_PROPERTY = "documentId";
  public static final long STATUS_CHECK_INTERVAL = 1000L;
  public static final long MAX_WAIT = 200000L;

  @Autowired
  private BoxClient boxClient;

  public GenericBoxConverterDocumentViewer() {
    super("generic-box");
  }

  @Override
  public void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {
    String uuid = upload(info, file, componentFactory);
    waitUntilDone(uuid);
    downloadThumbnail(componentFactory, uuid);
  }

  private String upload(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {
    String documentId;
    try {
      documentId = boxClient.uploadDocument(file.get());
    } catch (Exception e) {
      throw new IOException("Error while uploading to Box.", e);
    }
    componentFactory.createProperty(DOCUMENT_ID_PROPERTY, documentId);
    return documentId;
  }

  private void waitUntilDone(String documentId) throws IOException {
    DocumentStatus status;
    Map<String, Object> statusResponse = null;
    boolean timedOut = false;
    long startedAt = System.currentTimeMillis();
    do {
      try {
        Thread.sleep(STATUS_CHECK_INTERVAL);
      } catch (InterruptedException e) {
        throw new IOException("Interrupted while waiting for Box on document "
            + documentId + ". Last status: " + statusResponse, e);
      }
      try {
        status = boxClient.getDocumentStatus(documentId);
      } catch (Exception e) {
        throw new IOException(
            "Error while checking for Box status for document " + documentId
                + ". Last status: " + statusResponse, e);
      }
      timedOut = System.currentTimeMillis() - startedAt > MAX_WAIT;
    } while (!timedOut
        && (DocumentStatus.QUEUED == status || DocumentStatus.PROCESSING == status));
    if (timedOut) {
      throw new IOException("Timed out while waiting for Box on document "
          + documentId + ". Last status: " + statusResponse);
    }
    if (DocumentStatus.DONE != status) {
      throw new IOException("Box failed to process the document " + documentId
          + ". Status: " + status);
    }
  }

  private void downloadThumbnail(
      DocumentViewerComponentFactory componentFactory, String documentId)
      throws IOException {
    @Cleanup
    InputStream image = null;
    boolean timedOut = false;
    long startedAt = System.currentTimeMillis();
    do {
      try {
        Thread.sleep(STATUS_CHECK_INTERVAL);
      } catch (InterruptedException e) {
        throw new IOException("Interrupted while waiting for Box on document "
            + documentId + ".", e);
      }
      try {
        image = boxClient.getDocumentThumbnail(documentId, DocumentIconGenerator.SIZE,
            DocumentIconGenerator.SIZE);
      } catch (Exception e) {
        throw new IOException(
            "Error while downloading Box thumbnail for document " + documentId + ".", e);
      }
      timedOut = System.currentTimeMillis() - startedAt > MAX_WAIT;
    } while (!timedOut
        && image == null);
    if (timedOut) {
      throw new IOException("Timed out while waiting for Box on document thumbnail "
          + documentId + ".");
    }
    componentFactory.createPage("thumbnail.png",
        AssetMediaType.IMAGE_PNG, image);
  }
}
