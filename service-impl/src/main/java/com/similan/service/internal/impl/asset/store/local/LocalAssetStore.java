package com.similan.service.internal.impl.asset.store.local;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.store.AbstractAssetStore;

@Component
public class LocalAssetStore extends AbstractAssetStore {
  @Getter
  @Setter
  private File directory = null;

  public LocalAssetStore() {
    super("local");
  }

  private String generateKey(String category) {
    String uuid = UUID.randomUUID().toString();
    return category + "/" + uuid;
  }

  private File getFile(String key) {
    File directory = getDirectory();
    File file = new File(directory, key + ".asset");
    return file;
  }

  private void createDirectoryIfNeeded(String category) {
    File directory = getDirectory();
    File categoryDirectory = new File(directory, category);
    // if it fails, let createFile throw the exception
    categoryDirectory.mkdirs();
  }

  @Override
  public String create(String category, TemporaryFile file, StoredAssetMetadata metadata) throws IOException {
    createDirectoryIfNeeded(category);
    final String key = generateKey(category);
    final File permanentFile = getFile(key);

    Path path = permanentFile.toPath();
    Files.createFile(path);

    FileUtils.copyFile(file.get(), permanentFile);
    return key;
  }

  @Override
  public InputStream get(final String key) throws IOException {
    File file = getFile(key);
    return new FileInputStream(file);
  }

  @Override
  public void delete(String key) throws IOException {
    File file = getFile(key);
    Path path = file.toPath();
    Files.delete(path);
  }

  @PostConstruct
  public void initialize() {
    checkState(directory != null, "Directory not set");
    checkState(directory.exists(), "Directory does not exist: " + directory);
  }
}
