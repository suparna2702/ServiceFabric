package com.similan.framework.dto;

import java.io.Serializable;

import com.similan.domain.entity.util.NonmemberContactMessageType;

public class NonmemberContactMessageInfoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String comment;

  private String subject;

  private String firstName;

  private String lastName;

  private String email;

  private String business;

  private NonmemberContactMessageType messageType;

  public NonmemberContactMessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(NonmemberContactMessageType messageType) {
    this.messageType = messageType;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getBusiness() {
    return business;
  }

  public void setBusiness(String business) {
    this.business = business;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  @Override
  public String toString() {
    return "NonmemberContactMessageInfoDto [comment=" + comment + ", subject="
        + subject + ", firstName=" + firstName + ", lastName=" + lastName
        + ", email=" + email + ", business=" + business + ", messageType="
        + messageType + "]";
  }

}
