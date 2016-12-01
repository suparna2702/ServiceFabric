package com.similan.domain.common;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.apache.commons.lang.StringUtils;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public abstract class DomainBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  Long id;

  @Column(nullable = false)
  String uuid = UUID.randomUUID().toString();

  @Column
  Date createdOn;

  @Column
  Date modifiedOn;

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

    if (StringUtils.isBlank(uuid)) {
      uuid = UUID.randomUUID().toString();
    }
  }

  @PreUpdate
  protected void onModify() {
    modifiedOn = new Date();
  }
  
  @PostLoad
  protected void onLoad(){
    if (createdOn == null) {
      createdOn = new Date();
    }

    if (modifiedOn == null) {
      modifiedOn = new Date();
    }
    if (StringUtils.isBlank(uuid)) {
      uuid = UUID.randomUUID().toString();
    }
  }

}
