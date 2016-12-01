package com.similan.service.internal.impl.linkreference.alchemy;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceType;

@Component
@Slf4j
public class YouTubeClient {

  private static final String YOUTUBE_VIDEO_LIST_URL = "https://www.googleapis.com/youtube/v3/videos";
  private static final String YOUTUBE_VIDEO_SHARED_URL = "http://youtu.be/";
  private static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";

  private static final String YOUTUBE_PART_PARAMETER_SNIPPET = "snippet";
  private static final String YOUTUBE_MAX_RESULT = "50";
  private static final String YOUTUBE_DELETED_VIDEO = "Deleted video";
  private static final String YOUTUBE_VIDEO_PARAM_TITLE = "title";
  private static final String YOUTUBE_VIDEO__PARAM_DESCRIPTION = "description";
  private static final String YOUTUBE_VIDEO_PARAM_CONTENT_ITEMS = "items";

  @Autowired
  private PlatformCommonSettings platformCommonSettings;

  public boolean isYouTubeLink(String sharedUrl) {
    if (StringUtils.isBlank(sharedUrl)) {
      throw new IllegalArgumentException("URL cannot be empty");
    }

    if (StringUtils.contains(sharedUrl, YOUTUBE_VIDEO_SHARED_URL)
        || StringUtils.contains(sharedUrl, YOUTUBE_VIDEO_URL)) {
      return true;
    } else {
      return false;
    }
  }

  public LinkReferenceDto readLinkReferenceWithYouTube(String sharedUrl)
      throws URISyntaxException {

    if (StringUtils.isBlank(sharedUrl)) {
      throw new IllegalArgumentException("Video URL cannot be empty");
    }

    String videoId = StringUtils.EMPTY;

    if (StringUtils.contains(sharedUrl, YOUTUBE_VIDEO_SHARED_URL)) {
      videoId = StringUtils.removeStart(sharedUrl, YOUTUBE_VIDEO_SHARED_URL);
    }

    // if the videoId is equal to sharedUrl then it is
    // not finding the video id
    log.info("Video id " + videoId);
    if (StringUtils.equals(videoId, sharedUrl)) {
      if (StringUtils.contains(sharedUrl, YOUTUBE_VIDEO_URL)) {
        videoId = StringUtils.removeStart(sharedUrl, YOUTUBE_VIDEO_URL);
      } else {
        throw new IllegalArgumentException("URL does not contain Video Id "
            + sharedUrl);
      }
    }

    log.info("Video id " + videoId);
    URI uriVideoList = new URIBuilder(YOUTUBE_VIDEO_LIST_URL)
        .addParameter("part", YOUTUBE_PART_PARAMETER_SNIPPET)
        .addParameter("id", videoId)
        .addParameter("maxResults", YOUTUBE_MAX_RESULT)
        .addParameter("key",
            this.platformCommonSettings.getGoogleDeveloperApiKey().getValue())
        .build();
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
            String title = StringUtils.EMPTY;
            String imageUrl = StringUtils.EMPTY;
            String content = StringUtils.EMPTY;
            String url = sharedUrl;

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

              title = videoTitle;
              log.info("Video title " + videoTitle);

              // get description
              JsonElement descriptionElement = snippetObject
                  .get(YOUTUBE_VIDEO__PARAM_DESCRIPTION);
              content = descriptionElement.getAsString();

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
                    imageUrl = urlElement.getAsString();
                    log.info("Video Thumbnail URL " + urlElement.getAsString());
                  }
                }
              }

            }

            return LinkReferenceDto.builder().url(url).title(title)
                .content(content).imageUrl(imageUrl)
                .linkReferenceType(LinkReferenceType.YOUTUBE_VIDEO).build();
          }
        }
      }

    }

    return null;
  }

}
