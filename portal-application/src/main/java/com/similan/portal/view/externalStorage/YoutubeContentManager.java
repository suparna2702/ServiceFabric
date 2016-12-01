package com.similan.portal.view.externalStorage;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
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
@Component("youtubeContentManager")
@Slf4j
@Getter
@Setter
public class YoutubeContentManager extends ExternalContentManager {

  private static final long serialVersionUID = 1L;

  private static final String YOUTUBE_OAUTH_URL = "https://www.googleapis.com/auth/youtube.readonly";
  private static final String YOUTUBE_PLAYLIST_URL = "https://www.googleapis.com/youtube/v3/playlists";
  private static final String YOUTUBE_PLAYLIST_ITEM_URL = "https://www.googleapis.com/youtube/v3/playlistItems";
  private static final String YOUTUBE_VIDEO_LIST_URL = "https://www.googleapis.com/youtube/v3/videos";
  private static final String YOUTUBE_VIDEO_CHANNEL_LIST_URL = "https://www.googleapis.com/youtube/v3/channels";
  private static final String YOUTUBE_VIDEO_SHARED_URL = "http://youtu.be/";

  private static final String YOUTUBE_PART_PARAMETER_SNIPPET = "snippet";
  private static final String YOUTUBE_MAX_RESULT = "50";
  private static final String YOUTUBE_DELETED_VIDEO = "Deleted video";
  private static final String YOUTUBE_VIDEO_PARAM_ID = "videoId";
  private static final String YOUTUBE_VIDEO_PARAM_TITLE = "title";
  private static final String YOUTUBE_VIDEO__PARAM_DESCRIPTION = "description";
  private static final String YOUTUBE_VIDEO_PARAM_PUBLISH_DATE = "publishedAt";
  private static final String YOUTUBE_VIDEO_PARAM_CONTENT_DETAILS = "contentDetails";
  private static final String YOUTUBE_VIDEO_PARAM_CONTENT_ITEMS = "items";
  private static final String YOUTUBE_VIDEO_PARAM_RELATED_PLAYLISTS = "relatedPlaylists";
  private static final String YOUTUBE_VIDEO_PARAM_PLAYLIST_LIKES = "likes";
  private static final String YOUTUBE_VIDEO_PARAM_PLAYLIST_UPLOADS = "uploads";
  private static final String YOUTUBE_VIDEO_PARAM_PLAYLIST_WATCH_HISTORY = "watchHistory";
  private static final String YOUTUBE_VIDEO_PARAM_PLAYLIST_WATCH_LATER = "watchLater";

  public CloudImportFileDto getVideoFromSharedUrl(String sharedUrl)
      throws URISyntaxException {

    if (StringUtils.isBlank(sharedUrl)) {
      throw new IllegalArgumentException("Video URL cannot be empty");
    }

    if (StringUtils.containsAny(sharedUrl, YOUTUBE_VIDEO_SHARED_URL) != true) {
      throw new IllegalArgumentException(
          "This is not a valid Video URL. It must start with "
              + YOUTUBE_VIDEO_SHARED_URL);
    }

    String videoId = StringUtils.removeStart(sharedUrl,
        YOUTUBE_VIDEO_SHARED_URL);
    if (StringUtils.isBlank(videoId)) {
      throw new IllegalArgumentException("URL does not contain Video Id");
    }

    URI uriVideoList = new URIBuilder(YOUTUBE_VIDEO_LIST_URL)
        .addParameter("part", YOUTUBE_PART_PARAMETER_SNIPPET)
        .addParameter("id", videoId)
        .addParameter("maxResults", YOUTUBE_MAX_RESULT)
        .addParameter(
            "key",
            this.getPlatformCommonSettings().getGoogleDeveloperApiKey()
                .getValue()).build();
    log.info("Youtube Video List URL " + uriVideoList.toString());

    // send the request without any OAuth sign
    OAuthRequest request = new OAuthRequest(Verb.GET, uriVideoList.toString());
    request.setConnectionKeepAlive(false);
    Response response = request.send();

    JsonElement jsonElement = null;
    JsonParser parser = new JsonParser();
    jsonElement = parser.parse(response.getBody());
    if (jsonElement.isJsonObject()) {
      JsonObject rootObject = (JsonObject) jsonElement;
      JsonElement jsonElementItems = rootObject
          .get(YOUTUBE_VIDEO_PARAM_CONTENT_ITEMS);
      if (jsonElementItems != null && jsonElementItems.isJsonArray()) {

        JsonArray contents = (JsonArray) jsonElementItems;
        log.info("Number of video items " + contents.size());

        for (int i = 0; i < contents.size(); i++) {
          if (contents.get(i).isJsonObject()) {
            JsonObject file = (JsonObject) contents.get(i);
            CloudImportFileDto fileDto = new CloudImportFileDto();
            fileDto.setSource(ImportFileSource.YOUTUBE);
            fileDto.setExternallyManaged(true);

            // now we need to extract the snippet
            JsonElement snippetElement = file.get("snippet");
            if (snippetElement != null) {
              JsonObject snippetObject = (JsonObject) snippetElement;

              // get title
              JsonElement titleElement = snippetObject
                  .get(YOUTUBE_VIDEO_PARAM_TITLE);
              String videoTitle = titleElement.getAsString();
              if (videoTitle.equalsIgnoreCase(YOUTUBE_DELETED_VIDEO)) {
                // we do not want to show the deleted videos
                continue;
              }

              fileDto.setFileName(videoTitle);
              log.info("Video title " + videoTitle);
              fileDto.setFolder(false);

              // get description
              JsonElement descriptionElement = snippetObject
                  .get(YOUTUBE_VIDEO__PARAM_DESCRIPTION);
              fileDto.setDescription(descriptionElement.getAsString());

              // get the published date
              JsonElement dateElement = snippetObject
                  .get(YOUTUBE_VIDEO_PARAM_PUBLISH_DATE);
              if (dateElement != null) {
                Date creationDate = convertDriveDateToDate(dateElement
                    .getAsString());
                fileDto.setLastModified(creationDate);
              }

              // get thumb nail
              JsonElement thumbNailElement = snippetObject.get("thumbnails");
              if (thumbNailElement != null) {
                JsonObject thumbNailContents = (JsonObject) thumbNailElement;
                JsonElement defaultThumbNailElement = thumbNailContents
                    .get("default");
                if (defaultThumbNailElement != null) {
                  JsonObject defaultThumbNailElementObject = (JsonObject) defaultThumbNailElement;
                  JsonElement urlElement = defaultThumbNailElementObject
                      .get("url");
                  if (urlElement != null) {
                    fileDto.setThumbNailUrl(urlElement.getAsString());
                    log.info("Video Thumbnail URL " + urlElement.getAsString());
                  }
                }
              }

            }

            return fileDto;
          }
        }
      }

    }

    return null;
  }

  @Override
  public List<CloudImportFileDto> getRootFiles() throws URISyntaxException {

    URI uriPlayList = new URIBuilder(YOUTUBE_PLAYLIST_URL)
        .addParameter("part", YOUTUBE_PART_PARAMETER_SNIPPET)
        .addParameter("mine", "true")
        .addParameter("maxResults", YOUTUBE_MAX_RESULT)
        .addParameter(
            "key",
            this.getPlatformCommonSettings().getGoogleDeveloperApiKey()
                .getValue()).build();
    log.info("Youtube Playlist URL " + uriPlayList.toString());

    OAuthRequest request = new OAuthRequest(Verb.GET, uriPlayList.toString());
    this.getOauthService().signRequest(this.getAccessToken(), request);
    request.setConnectionKeepAlive(false);
    Response response = request.send();
    List<String> playLists = getPlayLists(response);

    List<CloudImportFileDto> allFiles = new ArrayList<CloudImportFileDto>();

    for (String playListId : playLists) {
      List<CloudImportFileDto> playListItemFiles = this
          .getPlayListItemsForPlayList(playListId, this.getAccessToken());
      log.info("Number of playlist items " + playListItemFiles.size()
          + " for play list id " + playListId);

      allFiles.addAll(playListItemFiles);
    }

    // now get special play lists (Watch History, Watch Later etc.)
    // there can be duplicate
    if (allFiles.size() <= 0) {
      List<String> channelPlayList = this.getChannelList();
      if (channelPlayList != null && channelPlayList.size() > 0) {
        for (String playListId : channelPlayList) {
          List<CloudImportFileDto> playListItemFiles = this
              .getPlayListItemsForPlayList(playListId, this.getAccessToken());
          log.info("Number of playlist items for special play list "
              + playListItemFiles.size() + " for play list id " + playListId);

          allFiles.addAll(playListItemFiles);
        }
      }

    }

    log.info("Number of total playlist items " + allFiles.size());
    return allFiles;
  }

  private List<String> getPlayLists(Response response) {

    List<String> playListIds = new ArrayList<String>();

    JsonElement jsonElement = null;
    JsonParser parser = new JsonParser();
    jsonElement = parser.parse(response.getBody());

    if (jsonElement.isJsonObject()) {
      JsonObject rootObject = (JsonObject) jsonElement;

      jsonElement = rootObject.get(YOUTUBE_VIDEO_PARAM_CONTENT_ITEMS);
      if (jsonElement != null && jsonElement.isJsonArray()) {

        JsonArray contents = (JsonArray) jsonElement;
        for (int i = 0; i < contents.size(); i++) {

          if (contents.get(i).isJsonObject()) {
            JsonObject file = (JsonObject) contents.get(i);

            JsonElement item = file.get("id");
            if (item != null) {
              playListIds.add(item.getAsString());
              log.debug("Playlist Id " + item.getAsString());
            }
          }
        }
      }
    }

    return playListIds;

  }

  private List<String> getChannelList() throws URISyntaxException {

    URI uriPlayList = new URIBuilder(YOUTUBE_VIDEO_CHANNEL_LIST_URL)
        .addParameter("part", YOUTUBE_VIDEO_PARAM_CONTENT_DETAILS)
        .addParameter("mine", "true")
        .addParameter("maxResults", YOUTUBE_MAX_RESULT)
        .addParameter(
            "key",
            this.getPlatformCommonSettings().getGoogleDeveloperApiKey()
                .getValue()).build();
    log.info("Youtube Playlist URL " + uriPlayList.toString());

    OAuthRequest request = new OAuthRequest(Verb.GET, uriPlayList.toString());
    this.getOauthService().signRequest(this.getAccessToken(), request);
    request.setConnectionKeepAlive(false);
    Response response = request.send();

    List<String> channelIdList = new ArrayList<String>();

    JsonElement jsonElement = null;
    JsonParser parser = new JsonParser();
    jsonElement = parser.parse(response.getBody());

    if (jsonElement != null && jsonElement.isJsonObject()) {

      JsonObject rootObject = (JsonObject) jsonElement;
      JsonElement jsonElementItems = rootObject
          .get(YOUTUBE_VIDEO_PARAM_CONTENT_ITEMS);
      if (jsonElementItems != null && jsonElementItems.isJsonArray()) {

        JsonArray contents = (JsonArray) jsonElementItems;
        for (int i = 0; i < contents.size(); i++) {

          if (contents.get(i).isJsonObject()) {
            JsonObject items = (JsonObject) contents.get(i);

            // get the content details
            JsonElement contentDetails = items
                .get(YOUTUBE_VIDEO_PARAM_CONTENT_DETAILS);
            if (contentDetails != null && contentDetails.isJsonObject()) {
              JsonObject contentDetailsObject = (JsonObject) contentDetails;
              JsonElement relatedPlayListsElement = contentDetailsObject
                  .get(YOUTUBE_VIDEO_PARAM_RELATED_PLAYLISTS);

              if (relatedPlayListsElement != null
                  && relatedPlayListsElement.isJsonObject()) {
                JsonObject relatedPlayListsElementObject = (JsonObject) relatedPlayListsElement;

                if (relatedPlayListsElementObject != null) {
                  JsonElement likePlayListId = relatedPlayListsElementObject
                      .get(YOUTUBE_VIDEO_PARAM_PLAYLIST_LIKES);
                  if (likePlayListId != null) {
                    channelIdList.add(likePlayListId.getAsString());
                    log.debug("Like Playlist Id "
                        + likePlayListId.getAsString());
                  }

                  JsonElement uploadPlayListId = relatedPlayListsElementObject
                      .get(YOUTUBE_VIDEO_PARAM_PLAYLIST_UPLOADS);
                  if (uploadPlayListId != null) {
                    channelIdList.add(uploadPlayListId.getAsString());
                    log.debug("Upload Playlist Id "
                        + uploadPlayListId.getAsString());
                  }

                  JsonElement watchHistoryPlayListId = relatedPlayListsElementObject
                      .get(YOUTUBE_VIDEO_PARAM_PLAYLIST_WATCH_HISTORY);
                  if (watchHistoryPlayListId != null) {
                    channelIdList.add(watchHistoryPlayListId.getAsString());
                    log.debug("Watch History Playlist Id "
                        + watchHistoryPlayListId.getAsString());
                  }

                  JsonElement watchLaterPlayListId = relatedPlayListsElementObject
                      .get(YOUTUBE_VIDEO_PARAM_PLAYLIST_WATCH_LATER);
                  if (watchLaterPlayListId != null) {
                    channelIdList.add(watchLaterPlayListId.getAsString());
                    log.debug("Watch Later Playlist Id "
                        + watchLaterPlayListId.getAsString());
                  }

                }

              }
            }
          }
        }
      }
    }

    return channelIdList;

  }

  private List<CloudImportFileDto> getPlayListItemsForPlayList(
      String playListId, Token token) throws URISyntaxException {

    URI uriPlayListItem = new URIBuilder(YOUTUBE_PLAYLIST_ITEM_URL)
        .addParameter("part", YOUTUBE_PART_PARAMETER_SNIPPET)
        .addParameter("playlistId", playListId)
        .addParameter("maxResults", YOUTUBE_MAX_RESULT)
        .addParameter(
            "key",
            this.getPlatformCommonSettings().getGoogleDeveloperApiKey()
                .getValue()).build();
    log.info("Youtube Playlist Item URL " + uriPlayListItem.toString());
    OAuthRequest requestPlayListItems = new OAuthRequest(Verb.GET,
        uriPlayListItem.toString());
    this.getOauthService().signRequest(this.getAccessToken(),
        requestPlayListItems);
    requestPlayListItems.setConnectionKeepAlive(false);

    Response response = requestPlayListItems.send();
    List<CloudImportFileDto> files = new ArrayList<CloudImportFileDto>();

    JsonElement jsonElement = null;
    JsonParser parser = new JsonParser();
    jsonElement = parser.parse(response.getBody());

    if (jsonElement != null && jsonElement.isJsonObject()) {

      JsonObject rootObject = (JsonObject) jsonElement;

      JsonElement jsonElementItems = rootObject.get("items");
      if (jsonElementItems != null && jsonElementItems.isJsonArray()) {

        JsonArray contents = (JsonArray) jsonElementItems;
        log.info("Number of video items " + contents.size());

        for (int i = 0; i < contents.size(); i++) {

          if (contents.get(i).isJsonObject()) {
            JsonObject file = (JsonObject) contents.get(i);
            CloudImportFileDto fileDto = new CloudImportFileDto();
            fileDto.setSource(ImportFileSource.YOUTUBE);
            fileDto.setExternallyManaged(true);

            // now we need to extract the snippet
            JsonElement snippetElement = file.get("snippet");
            if (snippetElement != null) {
              JsonObject snippetObject = (JsonObject) snippetElement;

              // get title
              JsonElement titleElement = snippetObject
                  .get(YOUTUBE_VIDEO_PARAM_TITLE);
              String videoTitle = titleElement.getAsString();
              if (videoTitle.equalsIgnoreCase(YOUTUBE_DELETED_VIDEO)) {
                // we do not want to show the deleted videos
                continue;
              }

              fileDto.setFileName(videoTitle);
              log.info("Video title " + videoTitle);
              fileDto.setFolder(false);

              // get description
              JsonElement descriptionElement = snippetObject
                  .get(YOUTUBE_VIDEO__PARAM_DESCRIPTION);
              fileDto.setDescription(descriptionElement.getAsString());

              // get the published date
              JsonElement dateElement = snippetObject
                  .get(YOUTUBE_VIDEO_PARAM_PUBLISH_DATE);
              if (dateElement != null) {
                Date creationDate = convertDriveDateToDate(dateElement
                    .getAsString());
                fileDto.setLastModified(creationDate);
              }

              // get video id
              JsonElement resourceElement = snippetObject.get("resourceId");
              if (resourceElement != null) {
                JsonObject resourceObject = (JsonObject) resourceElement;
                JsonElement resourceElementId = resourceObject
                    .get(YOUTUBE_VIDEO_PARAM_ID);
                if (resourceElementId != null) {
                  fileDto.setId(resourceElementId.getAsString());
                }
              }

              // get thumb nail
              JsonElement thumbNailElement = snippetObject.get("thumbnails");
              if (thumbNailElement != null) {
                JsonObject thumbNailContents = (JsonObject) thumbNailElement;
                JsonElement defaultThumbNailElement = thumbNailContents
                    .get("default");
                if (defaultThumbNailElement != null) {
                  JsonObject defaultThumbNailElementObject = (JsonObject) defaultThumbNailElement;
                  JsonElement urlElement = defaultThumbNailElementObject
                      .get("url");
                  if (urlElement != null) {
                    fileDto.setThumbNailUrl(urlElement.getAsString());
                    log.info("Video Thumbnail URL " + urlElement.getAsString());
                  }
                }
              }
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
      log.error("Cannot convert to date due to ", e);
      return new Date();
    }

  }

  @Override
  public List<CloudImportFileDto> getFilesForFolder(CloudImportFileDto data) {
    throw new UnsupportedOperationException(
        "YouTube does not have concept of folder");
  }

  @Override
  public void downloadFile(CloudImportFileDto data) throws Exception {
    throw new UnsupportedOperationException(
        "Cannot Download Video from YouTube for file " + data.getId());
  }

  @Override
  public OAuthService getAuthorizationService(String callbackUrl) {

    String apiKey = platformCommonSettings.getGoogleDeveloperClientId()
        .getValue();
    String apiSecret = platformCommonSettings.getGoogleDeveloperClientSecret()
        .getValue();

    ServiceBuilder builder = new ServiceBuilder();
    OAuthService youTubeOauthService = builder.provider(Google2Api.class)
        .apiKey(apiKey).apiSecret(apiSecret).callback(callbackUrl)
        .scope(YOUTUBE_OAUTH_URL).build();

    if (youTubeOauthService == null) {
      throw new NullPointerException(
          "Cannot get YouTube OAuth service with api key " + apiKey
              + " and api secret " + apiSecret);
    } else {
      log.info("The OAuth service " + youTubeOauthService.toString());
    }

    return youTubeOauthService;
  }

  @Override
  public ExternalServiceType getServiceType() {
    return ExternalServiceType.YOUTUBE;
  }
}
