package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "ExternalSocialPerson")
@DiscriminatorValue("ExternalSocialPerson")
@Getter
@Setter
@ToString
public class ExternalSocialPerson extends SocialActor {

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String mobilePhone;

  @Column
  private String primaryEmail;
  
  @Override
  public EntityType getEntityType() {
    return EntityType.EXTERNAL_SOCIAL_PERSON;
  }
  
  public String getImage() {
    return StringUtils.EMPTY;
  }
  
  @Override
  public String getDisplayName() {
    if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      return firstName + " " + lastName;
    } else if (StringUtils.isNotBlank(firstName) && StringUtils.isBlank(lastName)) {
      return firstName;
    } else if (StringUtils.isBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      return lastName;
    } else {
      return getName();
    }
  }
}
