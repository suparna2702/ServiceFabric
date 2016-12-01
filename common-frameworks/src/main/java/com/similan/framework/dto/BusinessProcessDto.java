package com.similan.framework.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BusinessProcessDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private BusinessProcessArtifactType artifactType;
	
	private String name;
	
	private Date timeStamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public BusinessProcessArtifactType getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(BusinessProcessArtifactType artifactType) {
		this.artifactType = artifactType;
	}
	
	public void setDeploymentTime(String timeStamp){
		
	}
	
	public String getDeploymentTime(){
		
		String retDate = "";
		if(this.timeStamp != null){
			SimpleDateFormat.getDateInstance().format(this.timeStamp);
		}
		
		return retDate;
	}

}
