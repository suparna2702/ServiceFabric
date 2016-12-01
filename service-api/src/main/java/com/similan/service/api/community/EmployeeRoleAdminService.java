package com.similan.service.api.community;

import java.util.List;

import com.similan.service.api.community.dto.basic.EmployeeRoleDto;
import com.similan.service.api.community.dto.basic.UpdateEmployeeRoleDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface EmployeeRoleAdminService {

  public List<EmployeeRoleDto> getAllRoles(SocialActorKey business);

  public void updateRole(UpdateEmployeeRoleDto role);
  
}
