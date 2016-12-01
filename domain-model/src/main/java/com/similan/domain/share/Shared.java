package com.similan.domain.share;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;

@MappedSuperclass
@ToString
@Getter
@Setter
public abstract class Shared implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor sharedBy;
  
  @Column
  private Date sharedDate;
  
  @Column(length = 500)
  private String sharedName = UUID.randomUUID().toString();

  @Column
  private String header;

  @Column(length = 500)
  private String message;
  
  private GenericReference<ISharable> sharedEntity;

}
