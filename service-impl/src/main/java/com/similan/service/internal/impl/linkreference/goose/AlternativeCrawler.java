package com.similan.service.internal.impl.linkreference.goose;

import com.gravity.goose.Configuration;
import com.gravity.goose.Crawler;
import com.gravity.goose.cleaners.DocumentCleaner;

public class AlternativeCrawler extends Crawler {
  public AlternativeCrawler(Configuration config) {
    super(config);
  }

  @Override
  public DocumentCleaner getDocCleaner() {
    return new AlternativeDocumentCleaner();
  }
}
