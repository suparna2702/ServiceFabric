package com.similan.service.impl.document.util;

import static com.google.common.base.Preconditions.checkState;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.crocodoc.Crocodoc;

@Component
public class CrocodocClient {
  public void setToken(String token) {
    Crocodoc.setApiToken(token);
  }

  public String getToken() {
    return Crocodoc.getApiToken();
  }

  @PostConstruct
  public void initialize() {
    checkState(Crocodoc.getApiToken() != null, "Crocodoc token not set.");
  }
}
