package com.similan.domain.entity.acccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BusinessAccountType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int upgradeSequence;
  @Column(nullable = false)
  private int employeeAllowances;
  @Column(nullable = false)
  private int collaborationWorkspaceAllowances;
  @Column(nullable = false)
  private int managementWorkspaceAllowances;
  @Column(nullable = false)
  private int partnerProgramAllowances;
  @Column(nullable = false)
  private int numberOfDocumentAllowances;
  @Column(nullable = false)
  private int storageSpaceAllowances;
  @Column(nullable = false)
  private int subscriptionFeePerMonth;
}
