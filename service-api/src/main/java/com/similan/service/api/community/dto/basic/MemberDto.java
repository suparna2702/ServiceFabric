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
public class MemberDto extends ActorDto {
  String firstName;
  String lastName;
  String businessName;

  @Deprecated
  String employer;

  protected MemberDto() {
  }

  public MemberDto(SocialActorKey key, String firstName, String lastName) {
    super(key);
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public SocialActorType getType() {
    return SocialActorType.Member;
  }

  public String getEmployer() {
    return employer;
  }

  @Deprecated
  public void setEmployer(String employer) {
    this.employer = employer;
  }

  @Deprecated
  public String getDisplayName() {
    if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      return firstName + " " + lastName;
    } else if (StringUtils.isNotBlank(firstName)
        && StringUtils.isBlank(lastName)) {
      return firstName;
    } else if (StringUtils.isBlank(firstName)
        && StringUtils.isNotBlank(lastName)) {
      return lastName;
    } else {
      return getKey().getName();
    }
  }
}
