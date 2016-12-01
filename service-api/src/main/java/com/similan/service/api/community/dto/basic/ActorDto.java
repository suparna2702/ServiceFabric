package com.similan.service.api.community.dto.basic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ActorDto extends KeyHolderDto<SocialActorKey> {
  @Deprecated
  Long id;
  @Deprecated
  String contactEmail;
  @Deprecated
  String displayImage;

  protected ActorDto() {
  }

  public ActorDto(SocialActorKey key) {
    super(key);
  }
  
  public abstract SocialActorType getType();

  @Deprecated
  public String getContactEmail() {
    return contactEmail;
  }

  @Deprecated
  public Long getId() {
    return id;
  }

  @Deprecated
  public void setId(Long id) {
    this.id = id;
  }
  
  @Deprecated
  public void setDisplayImage(String displayImage) {
    this.displayImage = displayImage;
  }
  
  @Deprecated
  public String getDisplayImage() {
    return displayImage;
  }

  @Deprecated
  public abstract String getDisplayName();
}
