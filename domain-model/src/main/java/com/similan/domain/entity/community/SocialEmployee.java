package com.similan.domain.entity.community;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.Setter;

import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Entity(name = "SocialEmployee")
@DiscriminatorValue("SocialEmployee")
@Getter
@Setter
public class SocialEmployee extends SocialContact {

  @Column
  @Enumerated(EnumType.STRING)
  private SocialEmployeeType employeeType;

  @Column
  @Enumerated(EnumType.STRING)
  @ElementCollection()
  @CollectionTable(joinColumns = @JoinColumn)
  private Set<EmployeeRole> roles = new HashSet<>();
  
  @Column
  private Boolean visible;

}
