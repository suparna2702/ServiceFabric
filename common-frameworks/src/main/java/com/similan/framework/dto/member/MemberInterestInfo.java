package com.similan.framework.dto.member;

import java.io.Serializable;

public class MemberInterestInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String interest;
	
	private boolean selected = false;
	
	public MemberInterestInfo(){
		id = Long.MIN_VALUE;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}
}
