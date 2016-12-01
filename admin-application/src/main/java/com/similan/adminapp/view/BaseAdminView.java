package com.similan.adminapp.view;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedProperty;

import lombok.Getter;
import lombok.Setter;

import com.similan.adminapp.service.AdminService;
import com.similan.adminapp.service.AffiliateLeadService;
import com.similan.adminapp.service.BusinessAdminService;
import com.similan.adminapp.service.BusinessProcessAdminService;
import com.similan.adminapp.service.MemberAdminService;
import com.similan.adminapp.service.MetaDataAdminService;
import com.similan.adminapp.service.ScriptExecutionService;
import com.similan.framework.util.JsfUtils;

@Getter @Setter
public abstract class BaseAdminView implements Serializable {
  private static final long serialVersionUID = 1L;

  @ManagedProperty("#{adminService}")
  protected transient AdminService adminService;

  @ManagedProperty("#{memberAdminService}")
  protected transient MemberAdminService memberAdminService;

  @ManagedProperty("#{businessAdminService}")
  protected transient BusinessAdminService businessAdminService;

  @ManagedProperty("#{bpmnAdminService}")
  protected transient BusinessProcessAdminService businessProcessService;

  @ManagedProperty("#{metaDataAdminService}")
  protected transient MetaDataAdminService metaDataAdminService;

  @ManagedProperty("#{affiliateLeadService}")
  protected transient AffiliateLeadService affiliateLeadService;

  @ManagedProperty("#{scriptExecutionService}")
  protected transient ScriptExecutionService scriptService;

  @ManagedProperty("#{facesHelper}")
  protected FacesHelper facesHelper;

  public ScriptExecutionService getScriptService() {
    return scriptService;
  }

  public void setScriptService(ScriptExecutionService scriptService) {
    this.scriptService = scriptService;
  }

  public AffiliateLeadService getAffiliateLeadService() {
    return affiliateLeadService;
  }

  public void setAffiliateLeadService(AffiliateLeadService affiliateLeadService) {
    this.affiliateLeadService = affiliateLeadService;
  }

  public MemberAdminService getMemberAdminService() {
    return memberAdminService;
  }

  public void setMemberAdminService(MemberAdminService memberAdminService) {
    this.memberAdminService = memberAdminService;
  }

  public BusinessAdminService getBusinessAdminService() {
    return businessAdminService;
  }

  public void setBusinessAdminService(BusinessAdminService businessAdminService) {
    this.businessAdminService = businessAdminService;
  }

  public AdminService getAdminService() {
    return adminService;
  }

  public void setAdminService(AdminService adminService) {
    this.adminService = adminService;
  }

  public FacesHelper getFacesHelper() {
    return facesHelper;
  }

  public void setFacesHelper(FacesHelper facesHelper) {
    this.facesHelper = facesHelper;
  }

  public BusinessProcessAdminService getBusinessProcessService() {
    return businessProcessService;
  }

  public void setBusinessProcessService(
      BusinessProcessAdminService businessProcessService) {
    this.businessProcessService = businessProcessService;
  }

  public MetaDataAdminService getMetaDataAdminService() {
    return metaDataAdminService;
  }

  public void setMetaDataAdminService(MetaDataAdminService metaDataAdminService) {
    this.metaDataAdminService = metaDataAdminService;
  }

  /**
   * 
   * @param in
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readObject(java.io.ObjectInputStream in) throws IOException,
      ClassNotFoundException {

    in.defaultReadObject();
    this.metaDataAdminService = (MetaDataAdminService) JsfUtils
        .findBackingBean("metaDataAdminService");
    this.adminService = (AdminService) JsfUtils.findBackingBean("adminService");
    this.memberAdminService = (MemberAdminService) JsfUtils
        .findBackingBean("memberAdminService");
    this.businessAdminService = (BusinessAdminService) JsfUtils
        .findBackingBean("businessAdminService");
    this.businessProcessService = (BusinessProcessAdminService) JsfUtils
        .findBackingBean("bpmnAdminService");
    this.affiliateLeadService = (AffiliateLeadService) JsfUtils
        .findBackingBean("affiliateLeadService");
    this.scriptService = (ScriptExecutionService) JsfUtils
        .findBackingBean("scriptExecutionService");
  }
}
