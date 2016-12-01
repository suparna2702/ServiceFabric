package com.similan.portal.oauth;

import lombok.extern.slf4j.Slf4j;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Slf4j
public class JsonAccessTokenExtractor implements AccessTokenExtractor {

  @Override
  public Token extract(String response) {
    try {
      JsonParser parser = new JsonParser();
      JsonElement jsonElement = parser.parse(response);
      if (jsonElement.isJsonObject()) {
        JsonObject rootObject = (JsonObject) jsonElement;
        jsonElement = rootObject.get("access_token");
        log.info("Access token " + jsonElement.getAsString());
        return new Token(jsonElement.getAsString(), "", response);
      }

    } catch (Exception e) {
      log.error("Failed to parse JSON:\n" + response, e);
    }
    return null;
  }
}
