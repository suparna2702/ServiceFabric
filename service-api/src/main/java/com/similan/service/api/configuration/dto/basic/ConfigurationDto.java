package com.similan.service.api.configuration.dto.basic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.similan.service.api.base.dto.basic.Dto;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto extends Dto {
  String baseUrl;
  String contactEmail;
}