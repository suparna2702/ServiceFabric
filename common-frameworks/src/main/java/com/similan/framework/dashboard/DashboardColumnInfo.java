package com.similan.framework.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "dashboardColumnInfo")
public class DashboardColumnInfo {
	
	private String columnName;
	
	private List<String> widgetList = new ArrayList<String>();
	
	@XmlElements(@XmlElement(name="dashboardColumns"))
	public List<String> getWidgetList() {
		return widgetList;
	}

	public void setWidgetList(List<String> widgetList) {
		this.widgetList = widgetList;
	}

	@XmlAttribute(name = "columnName")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
