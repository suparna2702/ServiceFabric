package com.similan.domain.entity.audit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.common.DomainBaseEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.audit.dto.basic.AuditEventType;

@Entity(name = "AuditEvent")
@Getter
@Setter
public class AuditEvent extends DomainBaseEntity {

  @Column
  private AuditEventType eventType;

  @ManyToOne
  private SocialActor member;

  @ManyToOne
  private SocialActor business;

  @JoinColumn
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<AuditEventAttribute> attributes = new ArrayList<AuditEventAttribute>();

}
