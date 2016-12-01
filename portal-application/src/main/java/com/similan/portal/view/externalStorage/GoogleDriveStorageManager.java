package com.similan.portal.view.externalStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import com.similan.portal.oauth.Google2Api;
import com.similan.service.api.externalservice.dto.ExternalServiceType;

@Scope("session")
@Component("googleDriveConnectionManager")
@Slf4j
@Getter
@Setter
public class GoogleDriveStorageManager extends ExternalContentManager {
  private static final String GOOGLE_DRIVE_OAUTH_URL = "https://www.googleapis.com/auth/drive.readonly";
  private static final String GOOGLE_DRIVE_FILES_REQUEST_URL = "https://www.googleapis.com/drive/v2/files?";
  private static final String GOOGLE_DRIVE_FILES_FOR_FOLDER_REQUEST_URL = "https://www.googleapis.com/drive/v2/files/";

  private static final String GOOGLE_FILE_REQUEST_FIELD_PARAMETERS = "fields=items(createdDate%2Cdescription%2CdownloadUrl%2CfileExtension%2CfileSize%2CiconLink%2Cid%2CmimeType%2CoriginalFilename%2Cowners%2FdisplayName%2Cthumbnail%2CthumbnailLink%2Ctitle)";
  private static final String GOOGLE_DRIVE_PARAMETER_ID = "id";
  private static final String GOOGLE_DRIVE_PARAMETER_TITLE = "title";
  private static final String GOOGLE_DRIVE_PARAMETER_DOWNLOAD_URL = "downloadUrl";
  private static final String GOOGLE_DRIVE_PARAMETER_FILESIZE = "fileSize";
  private static final String GOOGLE_DRIVE_PARAMETER_CREATION_DATE = "createdDate";
  private static final String GOOGLE_DRIVE_PARAMETER_MIMETYPE = "mimeType";
  private static final String GOOGLE_DRIVE_PARAMETER_THUMBNAIL_LINK = "thumbnailLink";
  private static final String GOOGLE_DRIVE_PARAMETER_DESCRIPTION = "description";
  private static final String GOOGLE_DRIVE_PARAMETER_GOOGLE_FOLDER = "application/vnd.google-apps.folder";

  private static final long serialVersionUID = 1L;

  @Override
  public List<CloudImportFileDto> getRootFiles() {
    OAuthRequest request = new OAuthRequest(Verb.GET,
        GOOGLE_DRIVE_FILES_REQUEST_URL + GOOGLE_FILE_REQUEST_FIELD_PARAMETERS);
    this.getOauthService().signRequest(this.getAccessToken(), request);

    Response response = request.send();
    return getFileListFromResponse(response, this.getAccessToken());
  }

  private List<CloudImportFileDto> getFileListFromResponse(Response response,
      Token token) {
    List<CloudImportFileDto> files = new ArrayList<CloudImportFileDto>();
    JsonElement jsonElement = null;
    JsonParser parser = new JsonParser();
    jsonElement = parser.parse(response.getBody());
    if (jsonElement.isJsonObject()) {

      JsonObject rootObject = (JsonObject) jsonElement;

      jsonElement = rootObject.get("items");
      if (jsonElement != null && jsonElement.isJsonArray()) {
        JsonArray contents = (JsonArray) jsonElement;
        for (int i = 0; i < contents.size(); i++) {
          if (contents.get(i).isJsonObject()) {
            JsonObject file = (JsonObject) contents.get(i);
            CloudImportFileDto fileDto = new CloudImportFileDto();
            fileDto.setSource(ImportFileSource.GOOGLEDRIVE);

            JsonElement item = file.get(GOOGLE_DRIVE_PARAMETER_ID);
            if (item != null) {
              fileDto.setId(item.getAsString());
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_THUMBNAIL_LINK);
            if (item != null) {
              fileDto.setThumbNailUrl(item.getAsString());
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_TITLE);
            if (item != null) {
              fileDto.setFileName(item.getAsString());
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_DESCRIPTION);
            if (item != null) {
              fileDto.setDescription(item.getAsString());
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_DOWNLOAD_URL);
            if (item != null) {
              fileDto.setDownloadUrl(item.getAsString());
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_FILESIZE);
            if (item != null) {
              fileDto.setSize(item.getAsLong());
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_CREATION_DATE);
            if (item != null) {
              fileDto
                  .setLastModified(convertDriveDateToDate(item.getAsString()));
            }

            item = file.get(GOOGLE_DRIVE_PARAMETER_MIMETYPE);
            if (item != null) {
              fileDto.setMimeType(item.getAsString());
              if (GOOGLE_DRIVE_PARAMETER_GOOGLE_FOLDER.equals(item
                  .getAsString()))
                fileDto.setFolder(true);
              else
                fileDto.setFolder(false);
            }

            fileDto.setToken(token);
            files.add(fileDto);
          }
        }
      }
    }

    return files;

  }

  private Date convertDriveDateToDate(String asString) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
          Locale.ENGLISH).parse(asString);
    } catch (Exception e) {
      return null;
    }

  }

  @Override
  public List<CloudImportFileDto> getFilesForFolder(CloudImportFileDto data) {

    log.debug("Getting Files for id " + data.getId());

    OAuthRequest request = new OAuthRequest(Verb.GET,
        GOOGLE_DRIVE_FILES_FOR_FOLDER_REQUEST_URL + data.getId() + "?"
            + GOOGLE_FILE_REQUEST_FIELD_PARAMETERS);
    this.getOauthService().signRequest(this.getAccessToken(), request);

    Response response = request.send();
    return getFileListFromResponse(response, this.getAccessToken());
  }

  @Override
  public void downloadFile(CloudImportFileDto data) throws Exception {

    if (StringUtils.isBlank(data.getDownloadUrl())) {
      if (log.isDebugEnabled()) {
        log.info("Download URL NULL cannot download file " + data.getFileName());
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
                "Cannot download file since download URL is not available "));
        return;
      }
    }

    OAuthRequest downloadRequest = new OAuthRequest(Verb.GET,
        data.getDownloadUrl());
    this.getOauthService().signRequest(this.getAccessToken(), downloadRequest);

    if (log.isDebugEnabled()) {
      log.info("Downloading file from Google Drive " + data.getFileName()
          + " download URL " + data.getDownloadUrl());
    }

    Response downloadResponse = downloadRequest.send();
    InputStream downloadStream = null;
    File resultFile = null;
    FileOutputStream fos = null;
    byte buffer[] = new byte[16384];
    try {
      resultFile = File.createTempFile(data.hashCode() + "", "");
      fos = new FileOutputStream(resultFile);
      downloadStream = downloadResponse.getStream();
      IOUtils.copyLarge(downloadStream, fos, buffer);
      data.setDownloadedFile(resultFile);
    } finally {
      IOUtils.closeQuietly(fos);
      IOUtils.closeQuietly(downloadStream);
    }
  }

  @Override
  public OAuthService getAuthorizationService(String callbackUrl) {

    String apiKey = platformCommonSettings.getGoogleDeveloperClientId()
        .getValue();
    String apiSecret = platformCommonSettings.getGoogleDeveloperClientSecret()
        .getValue();

    ServiceBuilder builder = new ServiceBuilder();
    OAuthService googleDriveOauthService = builder.provider(Google2Api.class)
        .apiKey(apiKey).apiSecret(apiSecret).callback(callbackUrl)
        .scope(GOOGLE_DRIVE_OAUTH_URL).build();

    if (googleDriveOauthService == null) {
      throw new NullPointerException(
          "Cannot get Google Drive OAuth service with api key " + apiKey
              + " and api secret " + apiSecret);
    } else {
      log.info("The OAuth service " + googleDriveOauthService.toString());
    }

    return googleDriveOauthService;
  }

  @Override
  public ExternalServiceType getServiceType() {
    return ExternalServiceType.GOOGLE_DRIVE;
  }
}
