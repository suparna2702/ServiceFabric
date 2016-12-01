package com.similan.domain.entity.community;

import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;

public enum EmployeeRole {
  BUSINESS_ADMIN,
  COLLABORATION_WORKSPACE_ADMIN(BUSINESS_ADMIN),
  MANAGEMENT_WORKSPACE_ADMIN(BUSINESS_ADMIN),
  PARTNER_PROGRAM_ADMIN(BUSINESS_ADMIN),
  REGULAR {
    @Override
    protected Set<EmployeeRole> buildAncestors(EmployeeRole[] parents) {
      return EnumSet.allOf(EmployeeRole.class);
    }
  };

  private EmployeeRole[] parents;
  @Getter
  private Set<EmployeeRole> ancestors;

  private EmployeeRole(EmployeeRole... parents) {
    this.parents = parents;
  }

  protected Set<EmployeeRole> buildAncestors(EmployeeRole[] parents) {
    Set<EmployeeRole> ancestors = EnumSet.of(this);
    for (EmployeeRole parent : parents) {
      ancestors.add(parent);
      ancestors.addAll(parent.getAncestors());
    }
    return ancestors;
  }

  static {
    for (EmployeeRole value : values()) {
      value.ancestors = value.buildAncestors(value.parents);
    }
  }
}
