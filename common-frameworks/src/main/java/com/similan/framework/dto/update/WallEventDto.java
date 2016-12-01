package com.similan.framework.dto.update;

import java.io.Serializable;
import java.util.Date;

public class WallEventDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Date timeStap;
	
	private String text;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStap() {
		return timeStap;
	}

	public void setTimeStap(Date timeStap) {
		this.timeStap = timeStap;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
