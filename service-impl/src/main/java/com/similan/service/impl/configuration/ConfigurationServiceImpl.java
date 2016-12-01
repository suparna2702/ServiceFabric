package com.similan.service.impl.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.configuration.ConfigurationService;
import com.similan.service.api.configuration.dto.basic.ConfigurationDto;
import com.similan.service.impl.base.ServiceImpl;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationServiceImpl extends ServiceImpl implements
    ConfigurationService {

  @Autowired
  PlatformCommonSettings platformCommonSettings;

  @Override
  public ConfigurationDto get() {
    String baseUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    String contactEmail = platformCommonSettings
        .getSuperAdminLoginForAdminApp().getValue();
    return new ConfigurationDto(baseUrl, contactEmail);
  }
}