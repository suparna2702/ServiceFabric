package com.similan.service.internal.impl.linkreference.goose;

import org.apache.commons.lang3.StringUtils;

import com.gravity.goose.Configuration;

public class AlternativeConfiguration extends Configuration {
  {
    setHtmlFetcher(new AlternativeHtmlFetcher());
    setImagemagickIdentifyPath(null);
  }

  @Override
  public boolean enableImageFetching() {
    return StringUtils.isNotBlank(imagemagickIdentifyPath())
        && super.enableImageFetching();
  }
}
