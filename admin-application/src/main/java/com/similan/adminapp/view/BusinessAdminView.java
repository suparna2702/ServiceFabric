package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.OrganizationBasicInfoDto;

@ViewScoped
@ManagedBean(name = "businessAdminView")
@Slf4j
public class BusinessAdminView extends BaseAdminView {

  private static final long serialVersionUID = 1L;

  private List<OrganizationBasicInfoDto> businessInfoList;

  private int numOfBusinesses = 0;

  @PostConstruct
  public void init() {
    log.info("Post init in business admin view");
    try {
      this.businessInfoList = this.getBusinessAdminService().getAllBusiness();
      if (businessInfoList != null) {
        numOfBusinesses = businessInfoList.size();
      }
    } catch (Exception exp) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Failed to get business due to " + exp.getMessage()));
    }
  }

  public int getNumOfBusinesses() {
    return numOfBusinesses;
  }

  public List<OrganizationBasicInfoDto> getBusinessInfoList() {
    return businessInfoList;
  }

  public void setBusinessInfoList(
      List<OrganizationBasicInfoDto> businessInfoList) {
    this.businessInfoList = businessInfoList;
  }

  public void upgradeBusinessAccount(Long orgId) {
    log.info("Upgrading Business account ");
    this.getBusinessAdminService().upgradeAccount(orgId);

  }

  public void downGradeAccount(Long orgId) {
    log.info("Down grade account Business account " + orgId);
    this.getBusinessAdminService().downgradeAccount(orgId);
  }

}
