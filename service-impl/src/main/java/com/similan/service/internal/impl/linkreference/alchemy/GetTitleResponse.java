package com.similan.service.internal.impl.linkreference.alchemy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTitleResponse extends AlchemyResponse {
  String url;
  String title;
}
