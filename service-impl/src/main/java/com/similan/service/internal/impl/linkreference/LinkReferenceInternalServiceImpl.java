package com.similan.service.internal.impl.linkreference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gravity.goose.Article;
import com.gravity.goose.Goose;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceType;
import com.similan.service.internal.api.linkreference.LinkReferenceInternalService;
import com.similan.service.internal.impl.linkreference.alchemy.AlchemyClient;
import com.similan.service.internal.impl.linkreference.alchemy.GetImageResponse;
import com.similan.service.internal.impl.linkreference.alchemy.GetTextResponse;
import com.similan.service.internal.impl.linkreference.alchemy.GetTitleResponse;
import com.similan.service.internal.impl.linkreference.alchemy.YouTubeClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class LinkReferenceInternalServiceImpl implements
    LinkReferenceInternalService {
  static final Pattern URL_PATTERN = Pattern
      .compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
          + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
          + "|mil|biz|info|mobi|name|aero|jobs|museum"
          + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
          + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
          + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
          + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
          + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
          + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
          + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");
  static final Pattern PROTOCOL_PATTERN = Pattern
      .compile("^(ht|f)tp(s?)\\:\\/\\/");

  @Autowired
  AlchemyClient alchemyClient;
  @Autowired
  Goose goose;
  @Autowired
  YouTubeClient youTubeClient;

  @Getter
  @Setter
  boolean useLocalExtractor = false;

  @Override
  public String extractUrl(String text) {
    Matcher matcher = URL_PATTERN.matcher(text);
    if (matcher.find()) {
      String url = matcher.group();
      if (!PROTOCOL_PATTERN.matcher(url).find()) {
        url = "http://" + url;
      }
      return url;
    }
    return null;
  }

  @Override
  public LinkReferenceDto readLinkReference(String url) {

    try {
      if (youTubeClient.isYouTubeLink(url)) {
        return youTubeClient.readLinkReferenceWithYouTube(url);
      }
    } catch (Exception exp) {
      log.error("Cannot extract YouTube URL", exp);
    }

    if (useLocalExtractor) {
      return readLinkReferenceWithGoose(url);
    } else {
      return readLinkReferenceWithAlchemy(url);
    }
  }

  private LinkReferenceDto readLinkReferenceWithGoose(String url) {
    Article article = goose.extractContent(url);
    return LinkReferenceDto
        .builder()
        .url(url)
        .title(article.title())
        .linkReferenceType(LinkReferenceType.WEBPAGE_ARTICLE)
        .content(article.cleanedArticleText())
        .imageUrl(
            StringUtils.isBlank(article.topImage().imageSrc()) ? null : article
                .topImage().imageSrc()).build();
  }

  private LinkReferenceDto readLinkReferenceWithAlchemy(String url) {
    GetTitleResponse titleResponse = alchemyClient.getTitle(url);
    GetTextResponse textResponse = alchemyClient.getText(url);
    GetImageResponse imageResponse = alchemyClient.getImage(url);
    return LinkReferenceDto
        .builder()
        .url(url)
        .title(titleResponse.getTitle())
        .linkReferenceType(LinkReferenceType.WEBPAGE_ARTICLE)
        .content(textResponse.getText())
        .imageUrl(
            StringUtils.isBlank(imageResponse.getImage()) ? null
                : imageResponse.getImage()).build();
  }
}
