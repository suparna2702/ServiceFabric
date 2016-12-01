package com.similan.service.internal.impl.linkreference.goose;

import java.io.File;

import com.gravity.goose.Article;
import com.gravity.goose.Configuration;
import com.gravity.goose.CrawlCandidate;
import com.gravity.goose.Crawler;
import com.gravity.goose.Goose;

public class AlternativeGoose extends Goose {
  private final Configuration config;

  public AlternativeGoose(Configuration config) {
    super(config);
    this.config = config;
    new File(config.localStoragePath()).setWritable(true, false);
  }

  @Override
  public Article sendToActor(CrawlCandidate crawlCandidate) {
    Crawler crawler = new AlternativeCrawler(config);
    return crawler.crawl(crawlCandidate);
  }
}
