package com.similan.service.impl.document;

import java.io.IOException;
import java.io.InputStream;

import com.similan.service.internal.api.asset.io.TemporaryFile;

public interface DerivedAssetFactory {
  void create(InputStream input);

  TemporaryFile createTemporaryFile() throws IOException;
}
