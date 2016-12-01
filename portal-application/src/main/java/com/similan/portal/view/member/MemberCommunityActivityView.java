package com.similan.portal.view.member;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.similan.domain.entity.event.LoginModeType;
import com.similan.framework.dto.CommunityEventDto;
import com.similan.portal.service.security.SecurityUtils;
import com.similan.portal.view.BaseView;

@ViewScoped
@ManagedBean(name = "memberCommunityActivityView")
public class MemberCommunityActivityView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private List<CommunityEventDto> longinInfoList;
	
	private Long averageSessionTime = Long.MIN_VALUE;
	
	private CartesianChartModel sessionCartesianModel = null;
	
	@PostConstruct
	public void init() {	
		this.longinInfoList = this.memberService.getLoginInfoList(SecurityUtils
				.getRequiredCurrentUserId());
		this.populateSessionGraphModel();
	}
	
	private void populateSessionGraphModel(){
		
		if(this.sessionCartesianModel == null){
			
			this.sessionCartesianModel = new CartesianChartModel();
			
			ChartSeries sessionTime = new ChartSeries();
			sessionTime.setLabel("Session Time");
			Integer sessionCount = 0;
			long totalSessionTime = 0;
			
			for(CommunityEventDto sessionEvent : longinInfoList){
				
				if(sessionEvent.getLoginInfo().getLoginMode()
					       .equals(LoginModeType.InCommunity) == true){
				
					if(sessionEvent.getLoginInfo().getLogoutTime() != null){
						
						long startTime = sessionEvent.getEventGenerationTime().getTime();
						long endTime = sessionEvent.getLoginInfo().getLogoutTime().getTime();
						long sessionDuration = (endTime - startTime)/1000;
						sessionTime.set(sessionCount.toString(), sessionDuration);
						sessionEvent.setSessionDuration(sessionDuration);
						totalSessionTime += sessionDuration;
						sessionCount++;
					}
				}
			}
			
			if(sessionCount != 0){
				averageSessionTime = ((totalSessionTime/sessionCount)/1000);
			}
			
			this.sessionCartesianModel.addSeries(sessionTime);
		}
	}
	
	public CartesianChartModel getSessionCartesianModel() {
		return sessionCartesianModel;
	}

	public void setSessionCartesianModel(CartesianChartModel sessionCartesianModel) {
		this.sessionCartesianModel = sessionCartesianModel;
	}

	public Long getAverageSessionTime() {
		
		return averageSessionTime;
	}

	public void setAverageSessionTime(Long averageSessionTime) {
		this.averageSessionTime = averageSessionTime;
	}

	public List<CommunityEventDto> getLonginInfoList() {
		return longinInfoList;
	}

	public void setLonginInfoList(List<CommunityEventDto> longinInfoList) {
		this.longinInfoList = longinInfoList;
	}
	
}
