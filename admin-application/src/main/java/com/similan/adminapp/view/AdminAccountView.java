package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.admin.AdminAccountDto;

@ViewScoped
@ManagedBean(name = "adminAccountView")
@Slf4j
public class AdminAccountView extends BaseAdminView {

	private static final long serialVersionUID = 1L;
	
	private List<AdminAccountDto> adminAccounts;

	public List<AdminAccountDto> getAdminAccounts() {
		return adminAccounts;
	}

	public void setAdminAccounts(List<AdminAccountDto> adminAccounts) {
		this.adminAccounts = adminAccounts;
	}
	
	@PostConstruct
	public void init() {
		
		log.info("Post init in admin account view");
		adminAccounts = this.adminService.getAllAdminAccount();
		
	}

}
