package com.similan.service.api.asset;

import java.io.InputStream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssetStream {
  AssetInfoDto info;
  InputStream input;
}
