package com.similan.service.rest.base;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestApplication extends Application {
  public static final String API_PATH = "api";
  public static final String API_DOCS_PATH = "/api-docs";

  @Autowired
  Set<SimilanApi> services;

  @Override
  public Set<Object> getSingletons() {
    return new HashSet<Object>(services);
  }
}