package com.similan.domain.entity.common;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class DomainEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  Long id;

  @Column(nullable = false)
  String uuid = UUID.randomUUID().toString();

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  Date createdOn;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  Date modifiedOn;

  public Long getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Date getModifiedOn() {
    return modifiedOn;
  }

  /**
   * Life cycle methods
   */
  @PrePersist
  protected void onCreate() {
    if (createdOn == null) {
      createdOn = new Date();
    }

    if (modifiedOn == null) {
      modifiedOn = new Date();
    }
  }

  @PreUpdate
  protected void onModify() {
    modifiedOn = new Date();
  }

}
