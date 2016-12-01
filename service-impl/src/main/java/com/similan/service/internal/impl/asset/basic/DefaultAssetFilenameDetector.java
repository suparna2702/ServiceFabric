package com.similan.service.internal.impl.asset.basic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.internal.impl.asset.AssetFilenameDetector;

@Component
public class DefaultAssetFilenameDetector implements AssetFilenameDetector {
  private static final String SIMPLE_ATTRIBUTE = "filename";
  private static final String EXTENDED_ATTRIBUTE = "filename*";

  @Override
  public String detect(String knownFilename, AssetMetadataDto metadata) {
    if (knownFilename != null) {
      knownFilename = knownFilename.trim();
    }
    if (knownFilename != null && !knownFilename.isEmpty()) {
      return knownFilename;
    }

    String contentDisposition = metadata.getContentDisposition();
    String filename = detect(contentDisposition, true);
    if (filename == null) {
      filename = detect(contentDisposition, false);
    }
    return filename;
  }

  private String detect(String contentDisposition, boolean extended) {
    String lowerCased = contentDisposition.toLowerCase();
    int indexOfAttribute = lowerCased.indexOf(extended ? EXTENDED_ATTRIBUTE
        : SIMPLE_ATTRIBUTE);
    if (indexOfAttribute == -1) {
      return null;
    }
    int endOfAttribute = indexOfAttribute + SIMPLE_ATTRIBUTE.length();
    int indexOfEquals = contentDisposition.indexOf('=', endOfAttribute);
    if (indexOfEquals == -1) {
      return null;
    }
    int startOfValue = indexOfEquals + 1;
    String encoding = null;
    String language = null;
    if (extended) {
      int indexOfFirstQuote = contentDisposition.indexOf('\'', indexOfEquals);
      if (indexOfFirstQuote != -1) {
        encoding = contentDisposition.substring(indexOfEquals + 1,
            indexOfFirstQuote).trim();
        if (encoding.isEmpty()) {
          encoding = null;
        }
        int indexOfSecondQuote = contentDisposition.indexOf('\'',
            indexOfFirstQuote + 1);
        if (indexOfSecondQuote != -1) {
          language = contentDisposition.substring(indexOfFirstQuote + 1,
              indexOfSecondQuote).trim();
          if (language.isEmpty()) {
            language = null;
          }
          startOfValue = indexOfSecondQuote + 1;
        } else {
          startOfValue = indexOfFirstQuote + 1;
        }
      }
    }
    String remainder = contentDisposition.substring(startOfValue).trim();
    if (remainder.isEmpty()) {
      return null;
    }
    int endOfValue;
    if (remainder.charAt(0) == '"') {
      endOfValue = remainder.indexOf('"', 1);
    } else {
      endOfValue = remainder.indexOf(' ');
    }
    if (endOfValue == -1) {
      endOfValue = remainder.length();
    }
    String value = remainder.substring(0, endOfValue).trim();
    if (value.isEmpty()) {
      return null;
    }
    if (value.charAt(0) == '"') {
      value = value.substring(1);
    }
    if (value.isEmpty()) {
      return null;
    }
    if (value.charAt(value.length() - 1) == '"') {
      value = value.substring(0, value.length() - 1);
    }
    value = value.trim();
    if (value.isEmpty()) {
      return null;
    }
    value = tryDecode(value, encoding, language);
    return value == null ? null : "<val=" + value + "> <enc=" + encoding
        + "> <lang=" + language + ">";
  }

  private String tryDecode(String value, String encoding, String language) {
    if (encoding != null) {
      try {
        return URLDecoder.decode(value, encoding);
      } catch (UnsupportedEncodingException e) {
      }
    }
    try {
      return URLDecoder.decode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
    }
    return value;
  }

}
