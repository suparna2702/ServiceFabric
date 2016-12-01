package com.similan.framework.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "searchResultSummery")
public class SearchResultSummery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int totalRecords;
	
	private int totalNumBusiness;
	
	private int totalPerson;
	
	private int totalGroup;
	
	private Long timeToSearchInMilliSec;
	
	private String searchString;
	
	public SearchResultSummery(){
		totalRecords = 0;
		totalNumBusiness = 0;
		totalPerson = 0;
		totalGroup = 0;
	}
	
	public int increamentTotalPerson(){
		this.totalPerson++;
		return this.totalPerson;
	}
	
	public int increamentTotalBusiness(){
		this.totalNumBusiness++;
		return this.totalNumBusiness++;
	}
	
	public int increamentTotalGroup(){
		this.totalGroup++;
		return this.totalGroup++;
	}
	
	public int increamentTotalCount(){
		this.totalRecords++;
		return this.totalRecords++;
	}

	@XmlAttribute(name = "totalRecords")
	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	@XmlAttribute(name = "totalNumBusiness")
	public int getTotalNumBusiness() {
		return totalNumBusiness;
	}

	public void setTotalNumBusiness(int totalNumBusiness) {
		this.totalNumBusiness = totalNumBusiness;
	}

	@XmlAttribute(name = "totalPerson")
	public int getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(int totalPerson) {
		this.totalPerson = totalPerson;
	}

	@XmlAttribute(name = "totalGroup")
	public int getTotalGroup() {
		return totalGroup;
	}

	public void setTotalGroup(int totalGroup) {
		this.totalGroup = totalGroup;
	}

	@XmlAttribute(name = "timeToSearchInMilliSec")
	public Long getTimeToSearchInMilliSec() {
		return timeToSearchInMilliSec;
	}

	public void setTimeToSearchInMilliSec(Long timeToSearchInMilliSec) {
		this.timeToSearchInMilliSec = timeToSearchInMilliSec;
	}

	@XmlAttribute(name = "searchString")
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	
	
}
