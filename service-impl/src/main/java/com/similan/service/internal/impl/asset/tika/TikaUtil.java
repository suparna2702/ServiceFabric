package com.similan.service.internal.impl.asset.tika;

import java.util.ArrayList;
import java.util.List;

import org.apache.tika.metadata.Metadata;

import com.similan.service.api.asset.dto.basic.AssetAdditionalMetadataDto;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;

public class TikaUtil {

  public static Metadata toTikaMetadata(AssetInfoDto info) {
    String filename = info.getFilename();
    AssetMetadataDto metadataDto = info.getMetadata();
    String contentType = metadataDto.getContentType();
    String contentDisposition = metadataDto.getContentDisposition();

    Metadata metadata = new Metadata();
    metadata.set(Metadata.RESOURCE_NAME_KEY, filename);
    metadata.set(Metadata.CONTENT_TYPE, contentType);
    metadata.set(Metadata.CONTENT_DISPOSITION, contentDisposition);
    return metadata;
  }

  public static AssetMetadataDto fromTikaMetadata(Metadata metadata) {
    String contentType = metadata.get(Metadata.CONTENT_TYPE);
    String contentDisposition = metadata.get(Metadata.CONTENT_DISPOSITION);

    String[] names = metadata.names();
    List<AssetAdditionalMetadataDto> detectedAdditionals = new ArrayList<AssetAdditionalMetadataDto>(
        names.length);
    for (String name : names) {
      String[] values = metadata.getValues(name);
      for (String value : values) {
        AssetAdditionalMetadataDto additional = new AssetAdditionalMetadataDto(
            name, value);
        detectedAdditionals.add(additional);
      }
    }

    AssetMetadataDto metadataDto = new AssetMetadataDto(contentType,
        contentDisposition, detectedAdditionals);
    return metadataDto;
  }

}
