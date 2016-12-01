package com.similan.domain.repository.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.admin.AdminAccount;
import com.similan.domain.repository.admin.jpa.JpaAdminAccountRepository;

@Repository
public class AdminAccountRepository {
  @Autowired
  private JpaAdminAccountRepository repository;
	
	public AdminAccount save(AdminAccount adminAccount) {
    return repository.save(adminAccount);
  }
	
	public AdminAccount findOne(Long id) {
    return repository.findOne(id);
  }
	
	public AdminAccount findByEmail(String email) {
    return repository.findByEmail(email);
  }
	
	public AdminAccount create(){
		AdminAccount account = new AdminAccount();
		return account;
	}

	public List<AdminAccount> findAll() {
    return repository.findAll();
  }
}
