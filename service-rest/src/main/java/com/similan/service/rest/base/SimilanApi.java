package com.similan.service.rest.base;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.core.PathSegment;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.IKey;

@Slf4j
public abstract class SimilanApi {
  public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

  public static final String REST_SEGMENT_LOCK = "lock";
  public static final String REST_SEGMENT_STATISTICS = "statistics";

  protected URI buildUri(IKey key, String... extraSegments) {
    return buildUri(key.getUri(), extraSegments);
  }

  private URI buildUri(String base, String... extraSegments) {
    StringBuilder builder = new StringBuilder(
        base.length() + extraSegments.length * 21);

    builder.append(base);
    append(builder, extraSegments);
    try {
      return new URI(builder.toString());
    } catch (URISyntaxException e) {
      // should not happen
      String message = "Error building uri: " + builder.toString();
      log.error(message, e);
      throw new IllegalArgumentException(message, e);
    }
  }

  private void append(StringBuilder builder, String... segments) {
    for (String segment : segments) {
      try {
        builder.append('/');
        builder.append(URLEncoder.encode(segment, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        // UTF8! this can't happen
        String message = "Error building uri: " + segment;
        log.error(message, e);
        throw new IllegalArgumentException(message, e);
      }
    }
  }

  protected <T extends IKey> T getKey(List<PathSegment> path, Class<T> bound) {
    String[] pathArray = new String[path.size()];
    int index = 0;
    for (PathSegment segment : path) {
      pathArray[index++] = segment.getPath();
    }
    IKey key = EntityType.getKey(pathArray);
    if (key == null) {
      throw new InvalidKeyException("Invalid key path: "
          + StringUtils.join(pathArray, '/'));
    }
    if (!bound.isInstance(key)) {
      throw new InvalidKeyException("Operation not supported on: "
          + StringUtils.join(pathArray, '/'));
    }
    return bound.cast(key);
  }
}
