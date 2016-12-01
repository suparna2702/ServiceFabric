package com.similan.portal.view.member;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.similan.portal.view.BaseView;

@ViewScoped
@ManagedBean(name = "memberDashBoardView")
public class MemberDashboardView extends BaseView {

	private static final long serialVersionUID = 1L;
	
    private DefaultDashboardModel model;
    
    private CartesianChartModel leadCartesianModel;
    
    private PieChartModel leadChartModel;
	
	@PostConstruct
	public void init() {		
		model = this.readDashboardModel();
		
		leadCartesianModel = new CartesianChartModel();
		ChartSeries clickThroughLeads = new ChartSeries();
		clickThroughLeads.setLabel("Click Through Leads");
		clickThroughLeads.set("2004", 120);
		clickThroughLeads.set("2005", 100);
		
		ChartSeries contactLeads = new ChartSeries();
		contactLeads.setLabel("Contact Leads");
		contactLeads.set("2004", 52);
		contactLeads.set("2005", 60);
		
		leadCartesianModel.addSeries(clickThroughLeads);
		leadCartesianModel.addSeries(contactLeads);
		
		leadChartModel = new PieChartModel();
		leadChartModel.set("Click Through Leads", 540);
		leadChartModel.set("Contact Leads", 325);
    }
	
	public CartesianChartModel getLeadCartesianModel() {
		return leadCartesianModel;
	}

	public void setLeadCartesianModel(CartesianChartModel leadCartesianModel) {
		this.leadCartesianModel = leadCartesianModel;
	}
	
	public PieChartModel getLeadChartModel() {
		return leadChartModel;
	}

	public void setLeadChartModel(PieChartModel leadChartModel) {
		this.leadChartModel = leadChartModel;
	}

	private DefaultDashboardModel readDashboardModel() {
		DefaultDashboardModel dashboardModel = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();
		
		column1.addWidget("lead");
		column1.addWidget("searchStatistics");
		dashboardModel.addColumn(column1);
		
		return dashboardModel;
	}

	public DefaultDashboardModel getModel() {
		return model;
	}

	public void setModel(DefaultDashboardModel model) {
		this.model = model;
	}
}
