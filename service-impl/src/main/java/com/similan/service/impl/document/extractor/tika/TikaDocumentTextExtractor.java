package com.similan.service.impl.document.extractor.tika;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import lombok.Cleanup;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.impl.document.DerivedAssetFactory;
import com.similan.service.impl.document.extractor.DocumentTextExtractor;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.tika.TikaUtil;

@Component
public class TikaDocumentTextExtractor implements DocumentTextExtractor {
  private static final Charset UTF8 = Charset.forName("UTF-8");

  @Autowired
  private Tika tika;

  @Override
  public void extract(AssetInfoDto info, TemporaryFile file,
      DerivedAssetFactory assetFactory) throws IOException {
    Metadata metadata = TikaUtil.toTikaMetadata(info);
    @Cleanup
    InputStream input = file.openInput();
    @Cleanup
    Reader reader = tika.parse(input, metadata);
    @Cleanup
    ReaderInputStream readerInput = new ReaderInputStream(reader, UTF8);
    assetFactory.create(readerInput);
  }
}
