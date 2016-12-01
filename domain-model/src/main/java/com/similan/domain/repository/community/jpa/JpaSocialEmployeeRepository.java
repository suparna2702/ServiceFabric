package com.similan.domain.repository.community.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;

public interface JpaSocialEmployeeRepository extends
    JpaRepository<SocialEmployee, Long> {

  @Query("delete SocialEmployee employment where employment.contactFrom = :organization and "
      + "(employment.contactTo = :employee)")
  void delete(@Param("organization") SocialOrganization organization,
      @Param("employee") SocialPerson employee);

  List<SocialEmployee> findByContactFrom(SocialOrganization actor);

  List<SocialEmployee> findByContactTo(SocialPerson actor);

  SocialEmployee findByContactToAndContactFromAndRolesIn(SocialPerson employee,
      SocialOrganization business, Set<EmployeeRole> roles);

  List<SocialEmployee> findByContactFromAndRolesIn(SocialOrganization business,
      Set<EmployeeRole> roles);

  SocialEmployee findByContactToAndContactFrom(SocialPerson employee,
      SocialOrganization business);

}
