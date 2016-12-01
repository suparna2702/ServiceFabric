package com.similan.framework.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dashboardLayoutInfo")
public class DashboardLayoutInfo {
	
	private List<DashboardColumnInfo> columnInfo = new ArrayList<DashboardColumnInfo>();

	@XmlElements(@XmlElement(name="dashboardColumns", type=DashboardColumnInfo.class))
	public List<DashboardColumnInfo> getColumnInfo() {
		return columnInfo;
	}

	public void setColumnInfo(List<DashboardColumnInfo> columnInfo) {
		this.columnInfo = columnInfo;
	}
}
