package com.similan.domain.repository.admin.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.admin.AdminAccount;

public interface JpaAdminAccountRepository 
                 extends JpaRepository<AdminAccount, Long>{
	
	@Query("select account from AdminAccount account where account.email=:email")
    AdminAccount findByEmail(@Param("email")String email);
}
