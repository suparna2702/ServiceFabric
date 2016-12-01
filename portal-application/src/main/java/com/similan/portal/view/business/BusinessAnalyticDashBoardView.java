package com.similan.portal.view.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.LeadType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.search.SearchStatisticsDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("businessAnalyticDashBoardView")
@Slf4j
public class BusinessAnalyticDashBoardView extends BaseView {

	private static final long serialVersionUID = 1L;
	private static final int SEARCH_YAXISMARGIN = 10;
	private static final int SEARCH_FROM_PERIOD = 30;
	private static final int SEARCH_FREQUENCY = 5;
	
	@Autowired(required=false)
	private OrganizationDetailInfoDto orgInfo;
	
	@Autowired(required=false)
	private MemberInfoDto memberInfo;
	
	private Integer searchPeriod = 20;
	
	private Long maxSearchCount = Long.valueOf("10");
	
	private List<SearchStatisticsDto> searchStatList;
	
	private List<Integer> periodList = new ArrayList<Integer>();
	
	private CartesianChartModel searchStatLinerModel;
	
	private PieChartModel leadPieChartModel;
	
    private CartesianChartModel partnerSearchModel;
	
	private CartesianChartModel pollModel;
	
	@PostConstruct
	public void init() {
		
		String orgId = this.getContextParam("oid");
		
		try {
			if(orgId != null) 
				orgInfo = this.getOrgService().getOrgById(Long.valueOf(orgId));
		} catch (Exception exp) {
			log.info("Error in fetching or with id " + orgId, exp);
		}
		
		if(orgInfo != null){
			
			Long businessActorId = orgInfo.getId();
			this.searchStatList = this.searchService
					                  .getSearchStatisticsByPeriod(SEARCH_FROM_PERIOD, 
					                		                       SEARCH_FREQUENCY, businessActorId);
			this.populateSearchStatLineModel();
			this.populateLeadPieChartModel();
			this.populatePartnerSearchModel();
			this.populatePollModel();
		}
		
	}
	
    private void populateLeadPieChartModel(){
    	
		Map<LeadType, Long> leadCountStat = null;
		//leadCountStat = this.getLeadService().getLeadCountStatSocialActor(this.orgInfo.getId());
		
		if(leadPieChartModel == null){
			
			leadPieChartModel = new PieChartModel();
			leadPieChartModel.set("Click Through Leads", leadCountStat.get(LeadType.ClickThroughLead));
			leadPieChartModel.set("Contact Leads", leadCountStat.get(LeadType.ContactLead));
			leadPieChartModel.set("Search Leads", leadCountStat.get(LeadType.SearchLead));
			leadPieChartModel.set("Acquired Leads", leadCountStat.get(LeadType.AcquiredLead));
			leadPieChartModel.set("Transferred Leads", leadCountStat.get(LeadType.TransferLead));
		}
			
	}
	
	private void populateSearchStatLineModel(){
		searchStatLinerModel = new CartesianChartModel();  
		  
        LineChartSeries searchedSeries = new LineChartSeries();  
        searchedSeries.setLabel("Searched");  
  
        LineChartSeries viewedSeries = new LineChartSeries();  
        viewedSeries.setLabel("Viewed");  
        viewedSeries.setMarkerStyle("diamond");  
        
        Integer counter = 0;
        for(SearchStatisticsDto searchStat : searchStatList){
        	counter++;
        	searchedSeries.set(counter, searchStat.getSearched());
        	log.info(" Searched counter " + counter + " searched number " + searchStat.getSearched());
        	
        	if(searchStat.getSearched() > this.maxSearchCount){
        		this.maxSearchCount = searchStat.getSearched();
        	}
        	
        	log.info(" Searched counter " + counter + " searched number " + searchStat.getViewed());
        	viewedSeries.set(counter, searchStat.getViewed());
        	
        	if(searchStat.getViewed() > this.maxSearchCount){
        		this.maxSearchCount = searchStat.getViewed();
        	}
        }
        
        //always add extra space above the line
        this.maxSearchCount += SEARCH_YAXISMARGIN;
        log.info("Max search count " +  this.maxSearchCount);
        
        searchStatLinerModel.addSeries(searchedSeries);  
        searchStatLinerModel.addSeries(viewedSeries);  
	}
	
	private void populatePollModel(){
		this.pollModel = new CartesianChartModel();  
		  
        LineChartSeries searchedSeries = new LineChartSeries();  
        searchedSeries.setLabel("Poll"); 
        
        Long businessActorId = orgInfo.getId();
        Map<String, Long> pollMap = this.orgService.getPollSubmissionCountOverTimeperiod(SEARCH_FROM_PERIOD, 
                SEARCH_FREQUENCY, businessActorId);
        
        Integer counter = 0;      
        for(Long pollStat : pollMap.values()){
        	counter++;
        	searchedSeries.set(counter, pollStat);
        	log.info(" poll counter " + counter + " poll number " + pollStat);
        }
        
        pollModel.addSeries(searchedSeries);  
	}
	
	private void populatePartnerSearchModel(){
		this.partnerSearchModel = new CartesianChartModel();  
		  
        LineChartSeries searchedSeries = new LineChartSeries();  
        searchedSeries.setLabel("Partner Search"); 
        
        Long businessActorId = orgInfo.getId();
        Map<String, Long> partnerSearchMap = this.orgService.getPartnerSearchEventCountOverTimeperiodByOrg(SEARCH_FROM_PERIOD, 
                SEARCH_FREQUENCY, businessActorId);
        
        Integer counter = 0;      
        for(Long searchStat : partnerSearchMap.values()){
        	counter++;
        	searchedSeries.set(counter, searchStat);
        	log.info(" Searched counter " + counter + " searched number " + searchStat);
        }
        
        partnerSearchModel.addSeries(searchedSeries);  

	}
	
	public CartesianChartModel getPartnerSearchModel() {
		return partnerSearchModel;
	}

	public void setPartnerSearchModel(CartesianChartModel partnerSearchModel) {
		this.partnerSearchModel = partnerSearchModel;
	}

	public CartesianChartModel getPollModel() {
		return pollModel;
	}

	public void setPollModel(CartesianChartModel pollModel) {
		this.pollModel = pollModel;
	}

	public Long getMaxSearchCount() {
		return maxSearchCount;
	}

	public void setMaxSearchCount(Long maxSearchCount) {
		this.maxSearchCount = maxSearchCount;
	}

	public Integer getSearchPeriod() {
		return searchPeriod;
	}

	public void setSearchPeriod(Integer searchPeriod) {
		this.searchPeriod = searchPeriod;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}

	public MemberInfoDto getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfoDto memberInfo) {
		this.memberInfo = memberInfo;
	}

	public List<SearchStatisticsDto> getSearchStatList() {
		return searchStatList;
	}

	public void setSearchStatList(List<SearchStatisticsDto> searchStatList) {
		this.searchStatList = searchStatList;
	}

	public List<Integer> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Integer> periodList) {
		this.periodList = periodList;
	}

	public CartesianChartModel getSearchStatLinerModel() {
		return searchStatLinerModel;
	}

	public void setSearchStatLinerModel(CartesianChartModel searchStatLinerModel) {
		this.searchStatLinerModel = searchStatLinerModel;
	}

	public PieChartModel getLeadPieChartModel() {
		return leadPieChartModel;
	}

	public void setLeadPieChartModel(PieChartModel leadPieChartModel) {
		this.leadPieChartModel = leadPieChartModel;
	}
	
	
}
