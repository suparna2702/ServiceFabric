package com.similan.portal.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.search.SearchStatisticsDto;

@Scope("request")
@Component("searchStatisticsComponentView")
public class SearchStatisticsComponentView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	@Autowired(required=false)
	private OrganizationDetailInfoDto orgInfo;
	
	@Autowired(required=false)
	private MemberInfoDto memberInfo;
	
	private List<SearchStatisticsDto> searchStatListMember;
	private List<SearchStatisticsDto> searchStatListBusiness;
	private List<Integer> periodList = new ArrayList<Integer>();
	
	@PostConstruct
	public void init() {
		
		periodList.add(1);
		periodList.add(7);
		periodList.add(30);
		
		Long memberActorId = Long.MIN_VALUE;
		Long businessActorId = Long.MIN_VALUE;
		
		if(orgInfo != null){
			businessActorId = orgInfo.getId();
			this.searchStatListBusiness = this.searchService.getSearchStatistics(periodList, businessActorId);
		}
		
		if(memberInfo != null) {
			memberActorId = memberInfo.getId();
			this.searchStatListMember = this.searchService.getSearchStatistics(periodList, memberActorId);
		}
	}

	public List<SearchStatisticsDto> getSearchStatListMember() {
		return searchStatListMember;
	}

	public void setSearchStatListMember(
			List<SearchStatisticsDto> searchStatListMember) {
		this.searchStatListMember = searchStatListMember;
	}

	public List<SearchStatisticsDto> getSearchStatListBusiness() {
		return searchStatListBusiness;
	}

	public void setSearchStatListBusiness(
			List<SearchStatisticsDto> searchStatListBusiness) {
		this.searchStatListBusiness = searchStatListBusiness;
	}

}
