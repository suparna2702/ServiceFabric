package com.similan.service.api.profile.dto;

import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExternalSocialPersonDto {

  @XmlAttribute
  private String primaryEmail;

  @XmlAttribute
  private String mobilePhone;

  @XmlAttribute
  private String phoneNumber;

  @XmlAttribute
  private String firstName;

  @XmlAttribute
  private String lastName;

  public ExternalSocialPersonDto(String primaryEmail, String mobilePhone,
      String phoneNumber, String firstName, String lastName) {

    this.primaryEmail = primaryEmail;
    this.mobilePhone = mobilePhone;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getDisplayName() {
    StringBuilder name = new StringBuilder().append(firstName).append(" ")
        .append(lastName);
    return name.toString();
  }

}
