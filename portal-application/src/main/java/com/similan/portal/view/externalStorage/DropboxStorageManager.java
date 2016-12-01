package com.similan.portal.view.externalStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.similan.framework.dto.fileImport.CloudImportFileDto;
import com.similan.framework.dto.fileImport.ImportFileSource;
import com.similan.portal.oauth.DropBox2Api;
import com.similan.service.api.externalservice.dto.ExternalServiceType;

@Scope("session")
@Component("dropboxConnectionManager")
@Slf4j
public class DropboxStorageManager extends ExternalContentManager {
  private static final String DROPBOX_FILE_METADATA_URL = "https://api.dropbox.com/1/metadata/dropbox";
  private static final String DROPBOX_DATE_TIME_FORMAT = "E, F MMM YYYY HH:mm:ss Z";
  private static final String DROPBOX_FILE_DOWNLOAD_BASE_URL = "https://api-content.dropbox.com/1/files/dropbox";
  private static final String DROPBOX_OAUTH_URL = "https://api.dropbox.com/1/metadata/dropbox?list=true";

  private static final long serialVersionUID = 1L;

  @PostConstruct
  public void init() {
    log.info("Initializing drop box manager");
  }

  public List<CloudImportFileDto> getRootFiles() {
    log.debug("Getting Root Files");

    OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, DROPBOX_OAUTH_URL);
    this.getOauthService().signRequest(this.getAccessToken(), oauthRequest);
    Response dropBoxResponse = oauthRequest.send();

    return getFileListFromResponse(dropBoxResponse, this.getAccessToken());
  }

  private List<CloudImportFileDto> getFileListFromResponse(
      Response fileResponse, Token token) {
    List<CloudImportFileDto> files = new ArrayList<CloudImportFileDto>();
    JsonElement jsonElement = null;
    JsonParser parser = new JsonParser();
    jsonElement = parser.parse(fileResponse.getBody());
    if (jsonElement.isJsonObject()) {

      JsonObject rootObject = (JsonObject) jsonElement;

      jsonElement = rootObject.get("contents");
      if (jsonElement.isJsonArray()) {
        JsonArray contents = (JsonArray) jsonElement;
        for (int i = 0; i < contents.size(); i++) {
          if (contents.get(i).isJsonObject()) {
            JsonObject file = (JsonObject) contents.get(i);
            CloudImportFileDto fileDto = new CloudImportFileDto();
            fileDto.setSource(ImportFileSource.DROPBOX);

            JsonElement item = file.get("path");
            if (item != null)
              fileDto.setId(item.getAsString());

            item = file.get("path");
            if (item != null)
              fileDto.setDownloadUrl(DROPBOX_FILE_DOWNLOAD_BASE_URL
                  + item.getAsString());

            item = file.get("mime_type");
            if (item != null)
              fileDto.setMimeType(item.getAsString());

            item = file.get("size");
            if (item != null)
              fileDto.setSize(item.getAsString());

            item = file.get("path");
            if (item != null)
              fileDto.setFileName(getFileNameFromPath(item.getAsString()));

            item = file.get("is_dir");
            if (item != null) {
              boolean isFolder = item.getAsBoolean();
              fileDto.setFolder(isFolder);
              if (isFolder) {
                fileDto.setMimeType("Folder");
                fileDto.setSize("-");
              }

            }

            item = file.get("modified");
            if (item != null)
              fileDto.setLastModified(convertDropboxDateToDate(item
                  .getAsString()));

            fileDto.setToken(token);
            files.add(fileDto);
          }
        }
      }
    }

    return files;

  }

  private Date convertDropboxDateToDate(String asString) {
    // "E, F MMM YYYY HH:mm:ss Z"
    try {
      return new SimpleDateFormat(DROPBOX_DATE_TIME_FORMAT, Locale.ENGLISH)
          .parse(asString);
    } catch (Exception e) {
      return null;
    }

  }

  private String getFileNameFromPath(String asString) {
    return asString.substring(asString.lastIndexOf('/') + 1);

  }

  public List<CloudImportFileDto> getFilesForFolder(CloudImportFileDto data) {
    log.debug("Getting Root Files for id " + data.getId());

    OAuthRequest request1 = new OAuthRequest(Verb.GET,
        DROPBOX_FILE_METADATA_URL + data.getId() + "?list=true");
    this.getOauthService().signRequest(this.getAccessToken(), request1);
    Response response1 = request1.send();

    return getFileListFromResponse(response1, this.getAccessToken());
  }

  public void downloadFile(CloudImportFileDto data) throws Exception {
    OAuthRequest request1 = new OAuthRequest(Verb.GET, data.getDownloadUrl());
    this.getOauthService().signRequest(this.getAccessToken(), request1);

    Response response1 = request1.send();
    InputStream downloadStream = null;
    File resultFile = null;
    FileOutputStream fos = null;
    byte buffer[] = new byte[16384];
    try {
      resultFile = File.createTempFile(data.hashCode() + "", "");
      fos = new FileOutputStream(resultFile);
      downloadStream = response1.getStream();
      IOUtils.copyLarge(downloadStream, fos, buffer);
      data.setDownloadedFile(resultFile);
    } finally {
      IOUtils.closeQuietly(fos);
      IOUtils.closeQuietly(downloadStream);
    }
  }

  @Override
  public OAuthService getAuthorizationService(String callbackUrl) {
    OAuthService dropBoxOuthService = new ServiceBuilder()
        .provider(DropBox2Api.class)
        .apiKey(platformCommonSettings.getDropboxApiKey().getValue())
        .apiSecret(platformCommonSettings.getDropboxApiSecretKey().getValue())
        .callback(callbackUrl).build();
    return dropBoxOuthService;
  }

  @Override
  public ExternalServiceType getServiceType() {
    return ExternalServiceType.DROPBOX;
  }

}
