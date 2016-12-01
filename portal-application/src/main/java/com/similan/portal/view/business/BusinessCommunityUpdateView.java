package com.similan.portal.view.business;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.OrganizationType;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("businessCommunityUpdateView")
public class BusinessCommunityUpdateView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private Long numReseller = Long.valueOf("0");
	
	private Long numDistributor = Long.valueOf("0");
	
	private Long numSupplier = Long.valueOf("0");
	
	private Long numLeadAffiliate = Long.valueOf("0");
	
	@PostConstruct
	public void init() {
		this.numReseller = this.orgService.getOrganizationCountByType(OrganizationType.RESELLER);
		this.numDistributor = this.orgService.getOrganizationCountByType(OrganizationType.DISTRIBUTOR);
		this.numSupplier = this.orgService.getOrganizationCountByType(OrganizationType.SUPPLIER);
		this.numLeadAffiliate = this.orgService.getOrganizationCountByType(OrganizationType.LEADAFFILIATE);
    }

	public Long getNumReseller() {
		return numReseller;
	}

	public void setNumReseller(Long numReseller) {
		this.numReseller = numReseller;
	}

	public Long getNumDistributor() {
		return numDistributor;
	}

	public void setNumDistributor(Long numDistributor) {
		this.numDistributor = numDistributor;
	}

	public Long getNumSupplier() {
		return numSupplier;
	}

	public void setNumSupplier(Long numSupplier) {
		this.numSupplier = numSupplier;
	}

	public Long getNumLeadAffiliate() {
		return numLeadAffiliate;
	}

	public void setNumLeadAffiliate(Long numLeadAffiliate) {
		this.numLeadAffiliate = numLeadAffiliate;
	}

}
