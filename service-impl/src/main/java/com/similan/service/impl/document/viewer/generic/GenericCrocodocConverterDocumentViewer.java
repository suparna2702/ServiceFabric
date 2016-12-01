package com.similan.service.impl.document.viewer.generic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import lombok.Cleanup;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.springframework.stereotype.Component;

import com.crocodoc.CrocodocDocument;
import com.crocodoc.CrocodocDownload;
import com.crocodoc.CrocodocException;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.impl.document.icon.DocumentIconGenerator;
import com.similan.service.impl.document.viewer.AbstractDocumentViewer;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

@Component
public class GenericCrocodocConverterDocumentViewer extends
    AbstractDocumentViewer {
  public static final String UUID_PROPERTY = "uuid";
  public static final long STATUS_CHECK_INTERVAL = 1000L;
  public static final long MAX_WAIT = 200000L;
  public static final String STATUS_API_PROPERTY = "status";
  public static final String VIEWABLE_API_PROPERTY = "viewable";
  public static final String ERROR_API_PROPERTY = "error";

  public enum CrocodocStatus {
    QUEUED, PROCESSING, DONE, ERROR;
  }

  public GenericCrocodocConverterDocumentViewer() {
    super("generic-crocodoc");
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
    String uuid;
    try {
      ContentType contentType = ContentType.create(info.getMediaType()
          .getDescriptor());
      FileBody fileBody = new FileBody(file.get(), contentType,
          info.getFilename());
      uuid = CrocodocDocument.upload(fileBody);
    } catch (CrocodocException e) {
      throw new IOException("Error while uploading to Crocodoc.", e);
    }
    componentFactory.createProperty(UUID_PROPERTY, uuid);
    return uuid;
  }

  private void waitUntilDone(String uuid) throws IOException {
    Object status;
    Map<String, Object> statusResponse = null;
    boolean timedOut = false;
    long startedAt = System.currentTimeMillis();
    do {
      try {
        Thread.sleep(STATUS_CHECK_INTERVAL);
      } catch (InterruptedException e) {
        throw new IOException(
            "Interrupted while waiting for Crocodoc on document " + uuid
                + ". Last status: " + statusResponse, e);
      }
      try {
        statusResponse = CrocodocDocument.status(uuid);
      } catch (CrocodocException e) {
        throw new IOException(
            "Error while checking for Crocodoc status for document " + uuid
                + ". Last status: " + statusResponse, e);
      }
      status = statusResponse.get(STATUS_API_PROPERTY);
      timedOut = System.currentTimeMillis() - startedAt > MAX_WAIT;
    } while (!timedOut
        && (CrocodocStatus.QUEUED.name().equals(status) || CrocodocStatus.PROCESSING.name()
            .equals(status)));
    if (timedOut) {
      throw new IOException("Timed out while waiting for Crocodoc on document "
          + uuid + ". Last status: " + statusResponse);
    }
    Object vieweable = statusResponse.get(VIEWABLE_API_PROPERTY);
    if (vieweable == null && !Boolean.TRUE.equals(vieweable)) {
      throw new IOException("Crocodoc failed to process the document " + uuid
          + ". Status: " + statusResponse);
    }
  }

  private void downloadThumbnail(
      DocumentViewerComponentFactory componentFactory, String uuid)
      throws IOException {
    HttpEntity entity;
    try {
      entity = CrocodocDownload.thumbnail(uuid, DocumentIconGenerator.SIZE,
          DocumentIconGenerator.SIZE);
    } catch (CrocodocException e) {
      throw new IOException(
          "Error while downloading Crocodoc thumbnail for document " + uuid
              + ".", e);
    }
    @Cleanup
    InputStream thumbnailInput = entity.getContent();
    componentFactory.createPage("thumbnail.png",
        AssetMediaType.IMAGE_PNG, entity.getContent());
  }
}
