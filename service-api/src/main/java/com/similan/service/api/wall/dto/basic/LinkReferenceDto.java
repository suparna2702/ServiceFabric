package com.similan.service.api.wall.dto.basic;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Builder;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkReferenceDto {
  String url;
  String title;
  String content;
  String imageUrl;
  LinkReferenceType linkReferenceType;
}
