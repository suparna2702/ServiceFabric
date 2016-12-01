package com.similan.service.api.community.dto.basic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.community.dto.key.SocialActorKey;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessDto extends ActorDto {
  String businessName;

  protected BusinessDto() {
  }

  public BusinessDto(SocialActorKey key, String businessName) {
    super(key);
    this.businessName = businessName;
  }

  public SocialActorType getType() {
    return SocialActorType.Business;
  }

  @Deprecated
  public String getDisplayName() {
    return StringUtils.isBlank(businessName) ? getKey().getName()
        : businessName;
  }
}
