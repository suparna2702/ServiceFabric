package com.similan.framework.dto;

import java.io.Serializable;

import com.similan.framework.dto.member.MemberInfoDto;

public class DomainSearchResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SearchResultType searchItemType;
	
	private MemberInfoDto memberInfo = null;
	
	private OrganizationDetailInfoDto orgInfo = null;
	
	private GroupInfo groupInfo = null;
	
	private boolean personType = false;
	
	private boolean organizationType = false;
	
	private boolean groupType = false;
	
	public DomainSearchResult(){
		
		personType = false;
		organizationType = false;
		groupType = false;

	}

	public boolean getPersonType() {
		return personType;
	}

	public void setPersonType(boolean personType) {
		this.personType = personType;
	}

	public boolean getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(boolean organizationType) {
		this.organizationType = organizationType;
	}

	public boolean getGroupType() {
		return groupType;
	}

	public void setGroupType(boolean groupType) {
		this.groupType = groupType;
	}

	public long getId(){
		
		long id = Long.MIN_VALUE;
		
		switch(searchItemType){
		    case  Person:
		    	  id = memberInfo.getId();
		          break;
		    case Organization:
		    	  id = orgInfo.getId();
		          break;
		    case Group:
		    	  id = groupInfo.getId();
		    	  break;
		    default: break;
		}
		
		return id;
	}
	
	public String getItemType(){
		return this.searchItemType.toString();
	}
	
	public void setItemType(String srchType){
		this.searchItemType = SearchResultType.valueOf(srchType);
	}
	
	public SearchResultType getSearchItemType() {
		return searchItemType;
	}

	public void setSearchItemType(SearchResultType searchItemType) {
		this.searchItemType = searchItemType;
	}

	public MemberInfoDto getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfoDto memberInfo) {
		this.memberInfo = memberInfo;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}

	public GroupInfo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}
	
}
