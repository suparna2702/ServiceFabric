package com.similan.service.api.asset.dto.basic;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

public enum AssetMediaMainType {

  UNKNOWN("*"),

  APPLICATION("application"),

  AUDIO("audio"),

  IMAGE("image"),

  MESSAGE("message"),

  MODEL("model"),

  MULTIPART("multipart"),

  TEXT("text"),

  VIDEO("video"),

  ;

  private final String descriptor;

  private static class StaticData {
    private static final Map<String, AssetMediaMainType> BY_DESCRIPTOR = new HashMap<String, AssetMediaMainType>();
  }

  private AssetMediaMainType(String descriptor) {
    this.descriptor = descriptor;
    checkArgument(StaticData.BY_DESCRIPTOR.put(descriptor, this) == null,
        "Duplicate media main type descriptor " + descriptor);
  }

  public String getDescriptor() {
    return descriptor;
  }

  public static AssetMediaMainType find(String descriptor) {
    String parsed = parse(descriptor);
    AssetMediaMainType mainType = StaticData.BY_DESCRIPTOR.get(parsed);
    if (mainType != null) {
      return mainType;
    }
    return UNKNOWN;
  }

  private static String parse(String descriptor) {
    if (descriptor == null) {
      return "*";
    }
    descriptor = descriptor.toLowerCase().trim();
    if (descriptor.isEmpty()) {
      return "*";
    }
    return descriptor;
  }
}
