package com.similan.service.internal.impl.asset.tika;

import java.io.IOException;
import java.io.InputStream;

import lombok.Cleanup;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.ExhaustiveAssetMetadataDetector;

@Component
public class TikaExhaustiveAssetMetadataDetector implements
    ExhaustiveAssetMetadataDetector {

  @Autowired
  private Tika tika;

  @Override
  public AssetMetadataDto detectMetadata(AssetInfoDto providedInfo, TemporaryFile file)
      throws IOException {
    Metadata metadata = TikaUtil.toTikaMetadata(providedInfo);
    detect(file, metadata);
    AssetMetadataDto detectedMetadata = TikaUtil.fromTikaMetadata(metadata);
    return detectedMetadata;
  }

  private void detect(TemporaryFile file, Metadata metadata) throws IOException {
    Parser parser = tika.getParser();
    ParseContext context = new ParseContext();
    context.set(Parser.class, parser);
    try {
      @Cleanup
      InputStream input = file.openInput();
      parser.parse(input, new DefaultHandler(), metadata,
          context);
    } catch (SAXException | TikaException e) {
      throw new IOException("Tika error while parsing:" + e, e);
    }
  }
}
