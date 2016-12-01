package com.similan.domain.repository.account.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.acccount.OrganizationAccount;
import com.similan.domain.entity.community.SocialOrganization;

public interface JpaOrganizationAccountRepository 
                 extends JpaRepository<OrganizationAccount, Long> {
	
	@Query("select orgAccount from OrganizationAccount orgAccount where orgAccount.owner=:org")
	public OrganizationAccount findByOrg(@Param("org")SocialOrganization org);

}
