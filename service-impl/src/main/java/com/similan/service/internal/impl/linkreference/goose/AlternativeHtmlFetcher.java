package com.similan.service.internal.impl.linkreference.goose;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import scala.Option;
import scala.Some;
import scala.util.Either;

import com.gravity.goose.Configuration;
import com.gravity.goose.network.AbstractHtmlFetcher;
import com.gravity.goose.network.HtmlFetcher;
import com.gravity.goose.network.HttpStatusValidator;
import com.gravity.goose.network.LoggableException;
import com.gravity.goose.network.MaxBytesException;

@SuppressWarnings("deprecation")
@Slf4j
public class AlternativeHtmlFetcher implements AbstractHtmlFetcher {
  private static final Pattern TITLE_PATTERN = Pattern.compile("<title>", Pattern.CASE_INSENSITIVE);
  
  @Override
  public Option<String> getHtml(Configuration config, String url) {
    // return HtmlFetcher.getHtml(config, url);

    HttpGet httpget = null;
    String htmlResult = null;
    HttpEntity entity = null;
    InputStream instream = null;

    // Identified the the apache http client does not drop URL fragments before
    // opening the request to the host
    // more info:
    // http://stackoverflow.com/questions/4251841/400-error-with-httpclient-for-a-link-with-an-anchor
    int foundAt = url.indexOf("#");
    String cleanUrl = foundAt >= 0 ? url.substring(0, foundAt) : url;
    String contentType = null;
    try {
      HttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.COOKIE_STORE,
          HtmlFetcher.emptyCookieStore());
      httpget = new HttpGet(cleanUrl);
      HttpClient httpClient = getHttpClient();
      HttpProtocolParams.setUserAgent(getHttpClient().getParams(),
          config.getBrowserUserAgent());

      HttpParams params = httpClient.getParams();
      HttpConnectionParams.setConnectionTimeout(params,
          config.getConnectionTimeout());
      HttpConnectionParams.setSoTimeout(params, config.getSocketTimeout());

      log.trace("Setting UserAgent To: "
          + HttpProtocolParams.getUserAgent(httpClient.getParams()));
      HttpResponse response = httpClient.execute(httpget, localContext);
      Header contentTypeHeader = response.getFirstHeader("Content-Type");
      contentType = contentTypeHeader.getValue();
      Either<Exception, String> validation = HttpStatusValidator.validate(
          cleanUrl, response.getStatusLine().getStatusCode());
      if (validation.isLeft()) {
        throw validation.left().get();
      }

      entity = response.getEntity();
      if (entity != null) {
        instream = entity.getContent();
        String encodingType = "UTF-8";
        try {
          encodingType = EntityUtils.getContentCharSet(entity);
          if (encodingType == null) {
            encodingType = "UTF-8";
          }
        } catch (Exception e) {
          if (log.isDebugEnabled()) {
            log.trace("Unable to get charset for: " + cleanUrl);
            log.trace("Encoding Type is: " + encodingType);
          }
        }
        try {
          htmlResult = HtmlFetcher.convertStreamToString(instream, 15728640,
              encodingType).trim();
        } finally {
          EntityUtils.consume(entity);
        }
      } else {
        log.trace("Unable to fetch URL Properly: " + cleanUrl);
      }
    } catch (NullPointerException e) {
      log.warn(e.toString() + " " + e.getMessage() + " Caught for URL: "
          + cleanUrl);
    } catch (MaxBytesException e) {
      log.trace("GRVBIGFAIL: " + cleanUrl + " Reached max bytes size");
      throw new RuntimeException(e);
    } catch (SocketException e) {
      log.warn(e.getMessage() + " Caught for URL: " + cleanUrl);
    } catch (SocketTimeoutException e) {
      log.trace(e.toString());
    } catch (LoggableException e) {
      log.warn(e.getMessage());
      return Some.empty();
    } catch (Exception e) {
      log.trace("FAILURE FOR LINK: " + cleanUrl + " " + e.toString());
      return Some.empty();
    } finally {
      if (instream != null) {
        try {
          instream.close();
        } catch (Exception e) {
          log.warn(e.getMessage() + " Caught for URL: " + cleanUrl);
        }
      }
      if (httpget != null) {
        try {
          httpget.abort();
          entity = null;
        } catch (Exception e) {
        }
      }
    }
    if (log.isDebugEnabled()) {
      log.debug("starting...");
    }
    if (htmlResult == null || htmlResult.length() < 1) {
      if (log.isDebugEnabled()) {
        log.debug("HTMLRESULT is empty or null");
      }
      throw new RuntimeException(cleanUrl);
    }
    InputStream is = null;
    String mimeType = null;
    try {
      is = new ByteArrayInputStream(htmlResult.getBytes("UTF-8"));
      mimeType = URLConnection.guessContentTypeFromStream(is);
      if (mimeType == null) {
        mimeType = contentType;
      }
      if (mimeType != null) {
        if ((mimeType == "text/html") == true
            || (mimeType == "application/xml") == true) {
          return Option.apply(htmlResult);
        } else {
          if (TITLE_PATTERN.matcher(htmlResult).find() == true) {
            return Option.apply(htmlResult);
          }
          log.trace("GRVBIGFAIL: " + mimeType + " - " + cleanUrl);
          throw new RuntimeException(cleanUrl);
        }
      } else {
        throw new RuntimeException(cleanUrl);
      }
    } catch (UnsupportedEncodingException e) {
      log.warn(e.getMessage() + " Caught for URL: " + cleanUrl);
    } catch (IOException e) {
      log.warn(e.getMessage() + " Caught for URL: " + cleanUrl);
    }
    return Option.empty();
  }

  @Override
  public HttpClient getHttpClient() {
    return HtmlFetcher.getHttpClient();
  }
}
