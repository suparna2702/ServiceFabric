package com.similan.service.internal.api.linkreference;

import com.similan.service.api.wall.dto.basic.LinkReferenceDto;

public interface LinkReferenceInternalService {
  String extractUrl(String text);

  LinkReferenceDto readLinkReference(String url);
}