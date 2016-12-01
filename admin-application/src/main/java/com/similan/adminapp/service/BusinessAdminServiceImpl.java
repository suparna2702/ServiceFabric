package com.similan.adminapp.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.impl.OrganizationManagementServiceImpl;

@Service("businessAdminService")
@Slf4j
public class BusinessAdminServiceImpl implements BusinessAdminService {
  

  @Autowired
  private OrganizationManagementServiceImpl organizationService;

  public OrganizationManagementServiceImpl getOrganizationService() {
    return organizationService;
  }

  public void setOrganizationService(
      OrganizationManagementServiceImpl organizationService) {
    this.organizationService = organizationService;
  }

  @Transactional
  public List<OrganizationBasicInfoDto> getAllBusiness() {
    return this.organizationService
        .getBusinessByVisibility(SocialMemberVisibilityType.VisiblePublic);
  }

  @Transactional
  public void upgradeAccount(Long orgId) {
    log.info("Upgrading account of org " + orgId);
    BusinessAccountType accountType = this.organizationService
        .getBusinessAccountByName(BusinessAccountSubscriptionType.BUSINESS_PLUS);
    
    this.organizationService.upgradeBusinessAccount(orgId, accountType);
  }

  @Override
  @Transactional
  public void downgradeAccount(Long orgId) {
    log.info("Downgrading account of org " + orgId);
    BusinessAccountType accountType = this.organizationService
        .getBusinessAccountByName(BusinessAccountSubscriptionType.BUSINESS_STANDARD);
    
    this.organizationService.downgradeBusinessAccount(orgId, accountType);
    
  }

}
