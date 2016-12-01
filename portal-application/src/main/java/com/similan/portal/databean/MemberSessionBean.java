package com.similan.portal.databean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@ManagedBean(name = "memberSessionBean")
@SessionScoped
public class MemberSessionBean extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private MemberInfoDto member = null;
	
	private OrganizationDetailInfoDto orgInfo = null;

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}
}
