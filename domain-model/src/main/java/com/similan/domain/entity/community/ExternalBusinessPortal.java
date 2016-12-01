package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.common.DomainEntity;

@Entity(name = "ExternalBusinessPortal")
@Getter
@Setter
@ToString
public class ExternalBusinessPortal extends DomainEntity {

  @Column
  private String portalName;

  @Column
  private String portalUrl;

  @ManyToOne
  @JoinColumn
  private SocialOrganization owner;

}
