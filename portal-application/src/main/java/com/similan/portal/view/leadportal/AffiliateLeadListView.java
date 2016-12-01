package com.similan.portal.view.leadportal;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("affiliateLeadListView")
public class AffiliateLeadListView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	private List<AffiliateLeadListDto> affiliateLeadList;
	
	@PostConstruct
	public void init() {
		this.affiliateLeadList = this.getAffiliateLeadService()
				                     .getAffiliateLeadListForBusiness(orgInfo);
	}

	public List<AffiliateLeadListDto> getAffiliateLeadList() {
		return affiliateLeadList;
	}

	public void setAffiliateLeadList(List<AffiliateLeadListDto> affiliateLeadList) {
		this.affiliateLeadList = affiliateLeadList;
	}
}
