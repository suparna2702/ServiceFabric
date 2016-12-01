package com.similan.service.internal.impl.asset.store;

import java.io.IOException;
import java.io.InputStream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Builder;
import lombok.experimental.FieldDefaults;

import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface AssetStore {
  @Getter
  @AllArgsConstructor
  @Builder
  @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
  public class StoredAssetMetadata {
    String contentType;
    String filename;
    boolean attachment;
  }

  String getId();

  String create(String category, TemporaryFile file, StoredAssetMetadata metadata)
      throws IOException;

  InputStream get(String key) throws IOException;

  void delete(String key) throws IOException;
}
