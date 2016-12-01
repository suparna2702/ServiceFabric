package com.similan.service.internal.impl.linkreference.alchemy;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class AlchemyClient {
  private static final String BASE_URL = "http://access.alchemyapi.com/calls/";

  private final RestTemplate template;

  @Setter
  @Getter
  private String apiKey;

  public AlchemyClient() {
    template = new RestTemplate();
  }

  private String url(String path) {
    return BASE_URL + path;
  }

  protected MultiValueMap<String, String> request() {
    MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
    request.add("apikey", apiKey);
    request.add("outputMode", "json");
    return request;
  }

  public GetImageResponse getImage(String url) {
    MultiValueMap<String, String> request = request();
    request.add("url", url);
    return template.postForObject(url("url/URLGetImage"), request,
        GetImageResponse.class);
  }
  
  public GetTextResponse getText(String url) {
    MultiValueMap<String, String> request = request();
    request.add("url", url);
    return template.postForObject(url("url/URLGetText"), request,
        GetTextResponse.class);
  }
  
  public GetTitleResponse getTitle(String url) {
    MultiValueMap<String, String> request = request();
    request.add("url", url);
    return template.postForObject(url("url/URLGetTitle"), request,
        GetTitleResponse.class);
  }
}
