package com.similan.service.internal.impl.asset.tika;

import java.io.IOException;
import java.io.InputStream;

import lombok.Cleanup;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.BasicAssetMetadataDetector;

@Component
public class TikaBasicAssetMetadataDetector implements
    BasicAssetMetadataDetector {

  @Autowired
  private Tika tika;

  @Override
  public String detect(AssetInfoDto providedInfo, TemporaryFile file)
      throws IOException {
    Metadata metadata = TikaUtil.toTikaMetadata(providedInfo);
    @Cleanup
    InputStream input = file.openInput();
    String detectedContentType = tika.detect(input, metadata);
    return detectedContentType;
  }

}
