package com.similan.service.api.externalservice.dto;

public enum ExternalServiceType {

  GOOGLE_DRIVE("Google Drive"),
  YOUTUBE("YouTube"),
  DROPBOX("Drop Box");

  private final String name;

  private ExternalServiceType(String serviceName) {
    name = serviceName;
  }

  public boolean equalsName(String otherName) {
    return (otherName == null) ? false : name.equals(otherName);
  }

  public String toString() {
    return name;
  }

}
