package com.similan.service.internal.impl.linkreference.alchemy;

import lombok.Data;

@Data
public abstract class AlchemyResponse {
  public enum Status {
    OK,
    ERROR
  }

  Status status;
  String statusInfo;
  String usage;
}
