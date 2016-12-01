package com.similan.framework.dto.member;

import java.io.Serializable;

public class MemberExpertiseInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id = 0;
	
	private String expertise;
	
    private boolean selected = false;
    
    public MemberExpertiseInfo(){
    	id = Long.MIN_VALUE;
    }
	
	public boolean isSelected() {
		return selected;
	}
	
	public boolean getSelected() {
		return this.selected;
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

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
}
