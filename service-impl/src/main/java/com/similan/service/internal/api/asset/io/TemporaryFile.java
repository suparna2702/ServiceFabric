package com.similan.service.internal.api.asset.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TemporaryFile implements Closeable {
  File file;
  
  public File get() {
    return file;
  }

  public InputStream openInput() throws IOException {
    return new FileInputStream(file);
  }

  public OutputStream openOutput() throws IOException {
    return new FileOutputStream(file);
  }

  @Override
  public void close() {
    file.delete();
  }
}
