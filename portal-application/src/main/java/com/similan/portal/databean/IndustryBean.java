package com.similan.portal.databean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import lombok.extern.slf4j.Slf4j;

import com.similan.domain.entity.metadata.IndustryType;
import com.similan.portal.view.BaseView;

@ManagedBean(name = "industryBean")
@ApplicationScoped
@Slf4j
public class IndustryBean extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private List<IndustryType> industryList = null;
	
	@PostConstruct
	public void init() {
		industryList = this.platformMetadataService.getAllIndustryType();
		log.info("Fetched number of industry " + industryList.size());
	}
	
	public List<IndustryType> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(List<IndustryType> industryList) {
		this.industryList = industryList;
	}
}
