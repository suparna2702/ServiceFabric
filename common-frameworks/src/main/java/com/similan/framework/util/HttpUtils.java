package com.similan.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtils {
  public static String buildContentDisposition(boolean attachment,
      String filename) {
    // find a library that does this.
    // Use: http://greenbytes.de/tech/webdav/rfc6266.html#examples
    // TODO: smarter replacements
    StringBuilder simpleNameBuilder = new StringBuilder(filename.length());
    for (int i = 0; i < filename.length(); i++) {
      char c = filename.charAt(i);
      if (c == '"') {
        simpleNameBuilder.append("'");
      }
      if (c == '\n' || c == '\r') {
        simpleNameBuilder.append(" ");
      } else {
        simpleNameBuilder.append(c);
      }
    }
    String simpleName = simpleNameBuilder.toString();
    // TODO: Use RFC 5987
    String properName;
    try {
      properName = URLEncoder.encode(filename, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.error("Error building content disposition: " + filename, e);
      properName = simpleName;
    }
    StringBuilder builder = new StringBuilder(simpleName.length()
        + properName.length() + 50);
    builder.append(attachment ? "attachment" : "inline");
    builder.append("; filename=\"");
    builder.append(simpleName);
    builder.append("\"; filename*=utf-8''");
    builder.append(properName);
    return builder.toString();
  }
}
