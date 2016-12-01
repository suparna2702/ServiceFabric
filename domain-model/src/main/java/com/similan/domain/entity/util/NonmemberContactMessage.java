package com.similan.domain.entity.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "NonmemberContactMessage")
public class NonmemberContactMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String email;

  @Column(length = 1000)
  private String comment;

  @Column
  private String subject;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String business;

  @Enumerated(EnumType.STRING)
  @Column
  private NonmemberContactMessageType messageType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
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

  public NonmemberContactMessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(NonmemberContactMessageType messageType) {
    this.messageType = messageType;
  }

}
