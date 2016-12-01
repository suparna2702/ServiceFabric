package com.similan.framework.box;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class BoxClient {
  public enum DocumentStatus {
    QUEUED,
    PROCESSING,
    DONE,
    ERROR;

    public static DocumentStatus parse(String text) {
      String status = text.toUpperCase();
      return DocumentStatus.valueOf(status);
    }
  }

  private static final String BASE_URL = "https://view-api.box.com/1";
  private static final String UPLOAD_BASE_URL = "https://upload.view-api.box.com/1";
  private static final int SESSION_DURATION = 60;

  @AllArgsConstructor
  @Setter
  @Getter
  static final class NewDocumentRequest {
    String url;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Setter
  @Getter
  static final class DocumentResponse {
    String id;
    String status;
  }

  @AllArgsConstructor
  @Setter
  @Getter
  static final class NewDocumentSessionRequest {
    String document_id;
    int duration;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Setter
  @Getter
  static final class NewDocumentSessionResponse {
    String id;
  }

  @Setter
  @Getter
  private String token;

  private RestTemplate restTemplate = new RestTemplate();

  public String uploadDocument(String url) throws IOException {
    HttpEntity<NewDocumentRequest> requestEntity = createRequestEntity(
        MediaType.MULTIPART_FORM_DATA, new NewDocumentRequest(url));
    ResponseEntity<DocumentResponse> responseEntity = restTemplate.exchange(
        BASE_URL + "/documents", HttpMethod.POST, requestEntity,
        DocumentResponse.class);
    DocumentResponse response = responseEntity.getBody();
    if (DocumentStatus.QUEUED == DocumentStatus.parse(response.getStatus())) {
      throw new IOException("Could not upload document: "
          + response.getStatus());
    }
    return response.getId();
  }

  public String uploadDocument(File file) throws IOException {
    Resource resource = new FileSystemResource(file);
    MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();

    request.add("file", resource);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = createRequestEntity(
        MediaType.MULTIPART_FORM_DATA, request);
    ResponseEntity<DocumentResponse> responseEntity = restTemplate.exchange(
        UPLOAD_BASE_URL + "/documents", HttpMethod.POST, requestEntity,
        DocumentResponse.class);
    DocumentResponse response = responseEntity.getBody();
    if (DocumentStatus.QUEUED != DocumentStatus.parse(response.getStatus())) {
      throw new IOException("Could not upload document: "
          + response.getStatus());
    }
    return response.getId();
  }

  public String createDocumentSession(String documentId) throws IOException {
    HttpEntity<NewDocumentSessionRequest> requestEntity = createRequestEntity(new NewDocumentSessionRequest(
        documentId, SESSION_DURATION));
    ResponseEntity<NewDocumentSessionResponse> responseEntity = restTemplate
        .exchange(BASE_URL + "/sessions", HttpMethod.POST, requestEntity,
            NewDocumentSessionResponse.class);
    NewDocumentSessionResponse response = responseEntity.getBody();
    return response.getId();
  }

  public DocumentStatus getDocumentStatus(String documentId) throws IOException {
    HttpEntity<Void> requestEntity = createRequestEntity(null);
    ResponseEntity<DocumentResponse> responseEntity = restTemplate.exchange(
        BASE_URL + "/documents/" + documentId, HttpMethod.GET, requestEntity,
        DocumentResponse.class);
    DocumentResponse response = responseEntity.getBody();
    return DocumentStatus.parse(response.getStatus());
  }

  public InputStream getDocumentThumbnail(String documentId, int width,
      int height) throws IOException {
    HttpEntity<Void> requestEntity = createRequestEntity(null);
    ResponseEntity<Resource> responseEntity = restTemplate.exchange(BASE_URL
        + "/documents/" + documentId + "/thumbnail?width=" + width + "&height="
        + height, HttpMethod.GET, requestEntity, Resource.class);
    if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
      return null;
    }
    if (responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new IOException("Could not retrieve document thumbnail: "
          + responseEntity.getStatusCode());
    }
    return responseEntity.getBody().getInputStream();
  }

  private <T> HttpEntity<T> createRequestEntity(T request) {
    return createRequestEntity(MediaType.APPLICATION_JSON, request);
  }

  private <T> HttpEntity<T> createRequestEntity(MediaType mediaType, T request) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Token " + token);
    headers.setContentType(mediaType);
    HttpEntity<T> requestEntity = new HttpEntity<>(request, headers);
    return requestEntity;
  }

  @PostConstruct
  public void initialize() {
    checkState(token != null, "Box token not set.");
  }
}
